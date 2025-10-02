from pydantic_settings import BaseSettings, SettingsConfigDict
from typing import List
from pathlib import Path


BASE_DIR = Path(__file__).resolve().parent.parent.parent.parent
ENV_FILE = BASE_DIR / ".env"


class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=str(ENV_FILE),
        env_file_encoding='utf-8',
        extra='ignore'
    )

    database_host: str
    database_name: str
    database_user: str
    database_password: str
    database_port: int = 3306

    cors_origins: str = "http://localhost:3000,http://localhost:5173"

    @property
    def database_url(self) -> str:
        return f"mysql+pymysql://{self.database_user}:{self.database_password}@{self.database_host}:{self.database_port}/{self.database_name}"

    @property
    def cors_origins_list(self) -> List[str]:
        return [origin.strip() for origin in self.cors_origins.split(",")]


settings = Settings()
