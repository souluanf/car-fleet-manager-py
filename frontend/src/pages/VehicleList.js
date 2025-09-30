import React, { useState, useEffect } from 'react';
import { vehicleService } from '../services/api';
import '../styles/VehicleList.css';

function VehicleList() {
  const [vehicles, setVehicles] = useState([]);
  const [filteredVehicles, setFilteredVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    veiculo: '',
    marca: '',
    ano: '',
    cor: ''
  });

  useEffect(() => {
    loadVehicles();
  }, []);

  const loadVehicles = async () => {
    try {
      setLoading(true);
      const response = await vehicleService.getAll();
      setVehicles(response.data);
      setFilteredVehicles(response.data);
      setError(null);
    } catch (err) {
      setError('Erro ao carregar veículos');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este veículo?')) {
      try {
        await vehicleService.delete(id);
        loadVehicles();
      } catch (err) {
        alert('Erro ao excluir veículo');
        console.error(err);
      }
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const applyFilters = () => {
    let filtered = vehicles;

    if (filters.veiculo) {
      filtered = filtered.filter(v =>
        v.veiculo.toLowerCase().includes(filters.veiculo.toLowerCase())
      );
    }
    if (filters.marca) {
      filtered = filtered.filter(v =>
        v.marca.toLowerCase().includes(filters.marca.toLowerCase())
      );
    }
    if (filters.ano) {
      filtered = filtered.filter(v => v.ano === parseInt(filters.ano));
    }
    if (filters.cor) {
      filtered = filtered.filter(v =>
        v.cor && v.cor.toLowerCase().includes(filters.cor.toLowerCase())
      );
    }

    setFilteredVehicles(filtered);
  };

  const clearFilters = () => {
    setFilters({ veiculo: '', marca: '', ano: '', cor: '' });
    setFilteredVehicles(vehicles);
  };

  if (loading) return <div className="loading">Carregando...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="vehicle-list-container">
      <div className="header">
        <h1>Veículos Cadastrados</h1>
        <button className="btn btn-primary" onClick={() => window.location.href = '/vehicles/new'}>
          + Novo Veículo
        </button>
      </div>

      <div className="filters-card">
        <h3>Filtros</h3>
        <div className="filters">
          <input
            type="text"
            name="veiculo"
            placeholder="Nome do veículo"
            value={filters.veiculo}
            onChange={handleFilterChange}
          />
          <input
            type="text"
            name="marca"
            placeholder="Marca"
            value={filters.marca}
            onChange={handleFilterChange}
          />
          <input
            type="number"
            name="ano"
            placeholder="Ano"
            value={filters.ano}
            onChange={handleFilterChange}
          />
          <input
            type="text"
            name="cor"
            placeholder="Cor"
            value={filters.cor}
            onChange={handleFilterChange}
          />
          <button className="btn btn-secondary" onClick={applyFilters}>Filtrar</button>
          <button className="btn btn-outline" onClick={clearFilters}>Limpar</button>
        </div>
      </div>

      <div className="vehicle-table">
        <table>
          <thead>
            <tr>
              <th>Veículo</th>
              <th>Marca</th>
              <th>Ano</th>
              <th>Descrição</th>
              <th>Cor</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {filteredVehicles.map(vehicle => (
              <tr key={vehicle.id}>
                <td>{vehicle.veiculo}</td>
                <td>{vehicle.marca}</td>
                <td>{vehicle.ano}</td>
                <td className="description">{vehicle.descricao}</td>
                <td>{vehicle.cor || '-'}</td>
                <td>
                  <span className={`status ${vehicle.vendido ? 'sold' : 'available'}`}>
                    {vehicle.vendido ? 'Vendido' : 'Disponível'}
                  </span>
                </td>
                <td className="actions">
                  <button
                    className="btn btn-small btn-edit"
                    onClick={() => window.location.href = `/vehicles/edit/${vehicle.id}`}
                  >
                    Editar
                  </button>
                  <button
                    className="btn btn-small btn-delete"
                    onClick={() => handleDelete(vehicle.id)}
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {filteredVehicles.length === 0 && (
          <div className="no-data">Nenhum veículo encontrado</div>
        )}
      </div>
    </div>
  );
}

export default VehicleList;
