from rest_framework import serializers
from .models import Product, Sale, SaleItem, Inventory
from apps.accounts.serializers import MemberSerializer

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ('id', 'name', 'category', 'description', 'price',
                 'stock_quantity', 'minimum_stock', 'image', 'barcode',
                 'created_at', 'updated_at')
        read_only_fields = ('id', 'created_at', 'updated_at')

class SaleItemSerializer(serializers.ModelSerializer):
    product = ProductSerializer(read_only=True)
    product_id = serializers.IntegerField(write_only=True)

    class Meta:
        model = SaleItem
        fields = ('id', 'product', 'product_id', 'quantity',
                 'unit_price', 'total_price')
        read_only_fields = ('id', 'total_price')

    def validate(self, data):
        product = Product.objects.get(id=data['product_id'])
        if product.stock_quantity < data['quantity']:
            raise serializers.ValidationError(
                f"Not enough stock. Available: {product.stock_quantity}"
            )
        return data

class SaleSerializer(serializers.ModelSerializer):
    member = MemberSerializer(read_only=True)
    member_id = serializers.IntegerField(write_only=True, required=False, allow_null=True)
    items = SaleItemSerializer(many=True, read_only=True)
    items_data = serializers.ListField(
        child=serializers.DictField(),
        write_only=True
    )

    class Meta:
        model = Sale
        fields = ('id', 'member', 'member_id', 'total_amount',
                 'payment_method', 'transaction_date', 'notes',
                 'items', 'items_data')
        read_only_fields = ('id', 'transaction_date', 'total_amount')

    def create(self, validated_data):
        items_data = validated_data.pop('items_data')
        
        # Calculer le montant total
        total_amount = 0
        for item_data in items_data:
            product = Product.objects.get(id=item_data['product_id'])
            quantity = item_data['quantity']
            total_amount += product.price * quantity

        # Créer la vente
        validated_data['total_amount'] = total_amount
        sale = Sale.objects.create(**validated_data)

        # Créer les articles de vente
        for item_data in items_data:
            product = Product.objects.get(id=item_data['product_id'])
            quantity = item_data['quantity']
            
            # Créer l'article
            SaleItem.objects.create(
                sale=sale,
                product=product,
                quantity=quantity,
                unit_price=product.price,
                total_price=product.price * quantity
            )
            
            # Mettre à jour le stock
            product.stock_quantity -= quantity
            product.save()
            
            # Enregistrer le mouvement d'inventaire
            Inventory.objects.create(
                product=product,
                quantity_change=-quantity,
                reason=f"Sale #{sale.id}"
            )

        return sale

class InventorySerializer(serializers.ModelSerializer):
    product = ProductSerializer(read_only=True)
    product_id = serializers.IntegerField(write_only=True)

    class Meta:
        model = Inventory
        fields = ('id', 'product', 'product_id', 'quantity_change',
                 'date', 'reason', 'notes')
        read_only_fields = ('id', 'date')

    def create(self, validated_data):
        product = Product.objects.get(id=validated_data['product_id'])
        inventory = Inventory.objects.create(**validated_data)
        
        # Mettre à jour le stock du produit
        product.stock_quantity += validated_data['quantity_change']
        product.save()
        
        return inventory
