# Generated by Django 5.2 on 2025-05-25 15:55

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ("accounts", "0002_user_poids_user_taille"),
    ]

    operations = [
        migrations.AddField(
            model_name="user",
            name="gender",
            field=models.CharField(blank=True, max_length=10),
        ),
    ]
