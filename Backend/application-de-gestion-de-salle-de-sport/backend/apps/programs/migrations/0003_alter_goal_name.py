# Generated by Django 5.2 on 2025-05-29 21:54

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ("programs", "0002_remove_goal_workout_type_alter_exercise_category"),
    ]

    operations = [
        migrations.AlterField(
            model_name="goal",
            name="name",
            field=models.CharField(
                choices=[
                    ("Perte de poids", "Perte de poids"),
                    ("Prise de masse", "Prise de masse"),
                    ("Maintien", "Maintien"),
                ],
                max_length=100,
            ),
        ),
    ]
