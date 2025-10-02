"""create_brands_table

Revision ID: 001
Revises:
Create Date: 2025-10-02

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa
from sqlalchemy import text


# revision identifiers, used by Alembic.
revision: str = '001'
down_revision: Union[str, None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    # Criar tabela brands
    op.create_table(
        'brands',
        sa.Column('id', sa.BINARY(16), nullable=False),
        sa.Column('name', sa.String(255), nullable=False),
        sa.Column('created', sa.DateTime(), server_default=sa.text('CURRENT_TIMESTAMP'), nullable=False),
        sa.Column('updated', sa.DateTime(), nullable=True),
        sa.PrimaryKeyConstraint('id'),
        sa.UniqueConstraint('name')
    )

    # Inserir dados das marcas
    brands = [
        'Acura', 'Agrale', 'Alfa Romeo', 'AM General', 'Asia Motors', 'Aston Martin',
        'Audi', 'Baby', 'BMW', 'BRM', 'Bugre', 'BYD', 'CAB Motors', 'Cadillac',
        'Chery', 'CBT Jipe', 'Chana', 'Changan', 'Chevrolet', 'Chrysler', 'Citroën',
        'Cross Lander', 'D2D Motors', 'Daewoo', 'Daihatsu', 'DFSK', 'Dodge', 'EFFA',
        'Engesa', 'Envemo', 'Ferrari', 'FEVER', 'Fiat', 'Fibravan', 'Ford', 'Foton',
        'Fyber', 'GAC', 'Geely', 'Great Wall', 'Gurgel', 'GWM', 'Hafei', 'Hitech Electric',
        'Honda', 'Hyundai', 'Isuzu', 'Iveco', 'JAC', 'Jaecoo', 'Jaguar', 'Jeep',
        'Jinbei', 'JPX', 'Kia', 'Lada', 'Lamborghini', 'Land Rover', 'Lexus', 'Lifan',
        'Lobini', 'Lotus', 'Mahindra', 'Maserati', 'Matra', 'Mazda', 'McLaren',
        'Mercedes-Benz', 'Mercury', 'MG', 'Mini', 'Mitsubishi', 'Miura', 'Neta',
        'Nissan', 'Omoda', 'Peugeot', 'Plymouth', 'Pontiac', 'Porsche', 'RAM',
        'Rely', 'Renault', 'Rolls-Royce', 'Rover', 'Saab', 'Saturn', 'Seat',
        'Seres', 'Shineray', 'Smart', 'SsangYong', 'Subaru', 'Suzuki', 'TAC',
        'Toyota', 'Troller', 'Volkswagen', 'Volvo', 'Wake', 'Walk', 'Zeekr'
    ]

    # Usar raw SQL para inserir com UUIDs gerados
    connection = op.get_bind()
    for brand_name in brands:
        connection.execute(
            text("INSERT INTO brands (id, name, created) VALUES (UUID_TO_BIN(UUID()), :name, CURRENT_TIMESTAMP)"),
            {"name": brand_name}
        )


def downgrade() -> None:
    op.drop_table('brands')
