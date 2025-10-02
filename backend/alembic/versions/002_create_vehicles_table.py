"""create_vehicles_table

Revision ID: 002
Revises: 001
Create Date: 2025-10-02

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = '002'
down_revision: Union[str, None] = '001'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    # Criar tabela vehicles
    op.create_table(
        'vehicles',
        sa.Column('id', sa.BINARY(16), nullable=False),
        sa.Column('veiculo', sa.String(255), nullable=False),
        sa.Column('brand_id', sa.BINARY(16), nullable=False),
        sa.Column('ano', sa.Integer(), nullable=False),
        sa.Column('descricao', sa.Text(), nullable=False),
        sa.Column('cor', sa.String(255), nullable=True),
        sa.Column('vendido', sa.Boolean(), nullable=False, server_default='0'),
        sa.Column('created', sa.DateTime(), server_default=sa.text('CURRENT_TIMESTAMP'), nullable=False),
        sa.Column('updated', sa.DateTime(), nullable=True),
        sa.PrimaryKeyConstraint('id'),
        sa.ForeignKeyConstraint(['brand_id'], ['brands.id'], name='fk_vehicles_brand')
    )


def downgrade() -> None:
    op.drop_table('vehicles')
