# Testes do Backend Python

Este diretório contém todos os testes do backend desenvolvido em Python/FastAPI.

## Estrutura de Testes

```
tests/
├── conftest.py                      # Fixtures compartilhadas
├── unit/                            # Testes unitários
│   ├── test_brand_service.py
│   ├── test_vehicle_service.py
│   ├── test_exercises_service.py
│   ├── test_brand_repository.py
│   └── test_vehicle_repository.py
└── integration/                     # Testes de integração
    ├── test_brand_router.py
    ├── test_vehicle_router.py
    └── test_exercises_router.py
```

## Tipos de Testes

### Testes Unitários
- **Services**: Testam a lógica de negócio isoladamente
- **Repositories**: Testam operações de banco de dados

### Testes de Integração
- **Routers**: Testam os endpoints da API (controllers)

## Executar Testes

### Todos os testes
```bash
pytest
```

### Testes com cobertura
```bash
pytest --cov=app --cov-report=html
```

### Apenas testes unitários
```bash
pytest tests/unit/
```

### Apenas testes de integração
```bash
pytest tests/integration/
```

### Teste específico
```bash
pytest tests/unit/test_vehicle_service.py
pytest tests/unit/test_vehicle_service.py::TestVehicleService::test_create_vehicle_success
```

### Modo verbose
```bash
pytest -v
```

### Com output detalhado
```bash
pytest -vv
```

## Cobertura de Testes

A cobertura mínima configurada é de **80%**.

Para gerar relatório de cobertura:
```bash
pytest --cov=app --cov-report=html
```

O relatório HTML estará disponível em `htmlcov/index.html`.

## Fixtures Disponíveis

- `test_db`: Banco de dados SQLite em memória
- `client`: Cliente de teste do FastAPI
- `sample_brand`: Marca de exemplo (Fiat)
- `sample_vehicle`: Veículo de exemplo (Uno)
- `multiple_brands`: Múltiplas marcas para testes
- `multiple_vehicles`: Múltiplos veículos para testes

## Dependências de Teste

```
pytest==8.3.4              # Framework de testes
pytest-cov==6.0.0          # Cobertura de código
pytest-asyncio==0.24.0     # Suporte a testes assíncronos
httpx==0.28.1              # Cliente HTTP para testes
faker==33.1.0              # Geração de dados fake
```

## Convenções

- Todos os arquivos de teste devem começar com `test_`
- Classes de teste devem começar com `Test`
- Métodos de teste devem começar com `test_`
- Use nomes descritivos que expliquem o que está sendo testado
- Organize testes seguindo o padrão AAA (Arrange, Act, Assert)

## Exemplos

### Teste Unitário
```python
def test_create_vehicle_success(self, vehicle_service, sample_brand):
    # Arrange
    request = VehicleRequest(...)

    # Act
    vehicle = vehicle_service.create_vehicle(request)

    # Assert
    assert vehicle.veiculo == "Uno"
```

### Teste de Integração
```python
def test_get_all_vehicles(self, client, multiple_vehicles):
    # Act
    response = client.get("/car-fleet-manager/api/v1/vehicles")

    # Assert
    assert response.status_code == 200
    assert len(response.json()) == 3
```
