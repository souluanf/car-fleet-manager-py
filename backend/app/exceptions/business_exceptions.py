class BusinessException(Exception):
    def __init__(self, message: str, status_code: int = 400):
        self.message = message
        self.status_code = status_code
        super().__init__(self.message)


class VehicleNotFoundException(BusinessException):
    def __init__(self, vehicle_id: str):
        super().__init__(f"Veículo com ID {vehicle_id} não encontrado", 404)


class InvalidBrandException(BusinessException):
    def __init__(self, brand_name: str):
        super().__init__(f"Marca '{brand_name}' é inválida", 400)


class InvalidVotingDataException(BusinessException):
    def __init__(self, message: str):
        super().__init__(message, 400)
