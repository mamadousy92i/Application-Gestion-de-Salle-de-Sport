# Generated by Django 5.1.5 on 2025-02-12 17:23

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='EquipmentCategory',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('description', models.TextField()),
            ],
            options={
                'verbose_name_plural': 'Equipment Categories',
            },
        ),
        migrations.CreateModel(
            name='Equipment',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=100)),
                ('description', models.TextField()),
                ('serial_number', models.CharField(max_length=100, unique=True)),
                ('purchase_date', models.DateField()),
                ('purchase_price', models.DecimalField(decimal_places=2, max_digits=10)),
                ('status', models.CharField(choices=[('available', 'Disponible'), ('in_use', 'En utilisation'), ('maintenance', 'En maintenance'), ('broken', 'Hors service')], default='available', max_length=20)),
                ('location', models.CharField(max_length=100)),
                ('last_maintenance_date', models.DateField(blank=True, null=True)),
                ('next_maintenance_date', models.DateField(blank=True, null=True)),
                ('notes', models.TextField(blank=True)),
                ('created_at', models.DateTimeField(auto_now_add=True)),
                ('updated_at', models.DateTimeField(auto_now=True)),
                ('category', models.ForeignKey(on_delete=django.db.models.deletion.PROTECT, to='equipment.equipmentcategory')),
            ],
        ),
        migrations.CreateModel(
            name='MaintenanceRecord',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('maintenance_date', models.DateField()),
                ('maintenance_type', models.CharField(choices=[('routine', 'Maintenance de routine'), ('repair', 'Réparation'), ('inspection', 'Inspection'), ('replacement', 'Remplacement')], max_length=20)),
                ('description', models.TextField()),
                ('cost', models.DecimalField(decimal_places=2, max_digits=10)),
                ('performed_by', models.CharField(max_length=100)),
                ('next_maintenance_date', models.DateField(blank=True, null=True)),
                ('notes', models.TextField(blank=True)),
                ('created_at', models.DateTimeField(auto_now_add=True)),
                ('equipment', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='equipment.equipment')),
            ],
        ),
    ]
