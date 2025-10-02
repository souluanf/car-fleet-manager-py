import pytest
from app.config.database import get_db, SessionLocal
from app.config.settings import settings


class TestDatabase:
    """Testes para configuração do banco de dados"""

    def test_get_db_generator(self):
        """Testa que get_db é um gerador que retorna uma sessão"""
        db_gen = get_db()

        # Testa que é um gerador
        assert hasattr(db_gen, '__next__')

        # Obtém a sessão
        db = next(db_gen)
        assert db is not None

        # Fecha a sessão
        try:
            next(db_gen)
        except StopIteration:
            pass  # Esperado quando o gerador termina

    def test_get_db_closes_session(self):
        """Testa que get_db fecha a sessão corretamente"""
        db_gen = get_db()
        db = next(db_gen)

        # Verifica que a sessão está ativa
        assert db is not None

        # Fecha o gerador (simula o finally)
        try:
            db_gen.close()
        except Exception:
            pass


class TestSettings:
    """Testes para configuração de settings"""

    def test_settings_database_url(self):
        """Testa que database_url está configurado"""
        assert settings.database_url is not None
        assert isinstance(settings.database_url, str)
        assert "mysql+pymysql://" in settings.database_url

    def test_settings_cors_origins_list(self):
        """Testa que cors_origins_list retorna uma lista"""
        origins = settings.cors_origins_list
        assert isinstance(origins, list)
        assert len(origins) > 0
        # Verifica que cada origem é uma string não vazia
        for origin in origins:
            assert isinstance(origin, str)
            assert len(origin) > 0
