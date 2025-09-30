import React, { useState, useEffect } from 'react';
import { vehicleService } from '../services/api';
import '../styles/Statistics.css';

function Statistics() {
  const [statistics, setStatistics] = useState(null);
  const [byDecade, setByDecade] = useState(null);
  const [byBrand, setByBrand] = useState(null);
  const [lastWeek, setLastWeek] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStatistics();
  }, []);

  const loadStatistics = async () => {
    try {
      setLoading(true);
      const [statsRes, decadeRes, brandRes, weekRes] = await Promise.all([
        vehicleService.getStatistics(),
        vehicleService.getByDecade(),
        vehicleService.getByBrand(),
        vehicleService.getLastWeek()
      ]);

      setStatistics(statsRes.data);
      setByDecade(decadeRes.data);
      setByBrand(brandRes.data);
      setLastWeek(weekRes.data);
    } catch (err) {
      console.error('Erro ao carregar estatísticas:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Carregando estatísticas...</div>;
  }

  return (
    <div className="statistics-container">
      <h1>Estatísticas</h1>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">🚗</div>
          <div className="stat-content">
            <h3>Total de Veículos</h3>
            <p className="stat-number">{statistics?.totalVehicles || 0}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">✅</div>
          <div className="stat-content">
            <h3>Veículos Vendidos</h3>
            <p className="stat-number">{statistics?.soldVehicles || 0}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">🔵</div>
          <div className="stat-content">
            <h3>Veículos Disponíveis</h3>
            <p className="stat-number">{statistics?.unsoldVehicles || 0}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">📅</div>
          <div className="stat-content">
            <h3>Cadastrados esta Semana</h3>
            <p className="stat-number">{lastWeek?.total || 0}</p>
          </div>
        </div>
      </div>

      <div className="charts-row">
        <div className="chart-card">
          <h2>Distribuição por Década</h2>
          <div className="chart-content">
            {byDecade?.vehiclesByDecade && Object.keys(byDecade.vehiclesByDecade).length > 0 ? (
              <div className="bar-chart">
                {Object.entries(byDecade.vehiclesByDecade)
                  .sort(([a], [b]) => a.localeCompare(b))
                  .map(([decade, count]) => (
                    <div key={decade} className="bar-item">
                      <div className="bar-label">{decade}</div>
                      <div className="bar-container">
                        <div
                          className="bar-fill"
                          style={{
                            width: `${(count / Math.max(...Object.values(byDecade.vehiclesByDecade))) * 100}%`
                          }}
                        >
                          <span className="bar-value">{count}</span>
                        </div>
                      </div>
                    </div>
                  ))}
              </div>
            ) : (
              <div className="no-data">Nenhum dado disponível</div>
            )}
          </div>
        </div>

        <div className="chart-card">
          <h2>Distribuição por Fabricante</h2>
          <div className="chart-content">
            {byBrand?.vehiclesByBrand && Object.keys(byBrand.vehiclesByBrand).length > 0 ? (
              <div className="bar-chart">
                {Object.entries(byBrand.vehiclesByBrand)
                  .sort(([, a], [, b]) => b - a)
                  .map(([brand, count]) => (
                    <div key={brand} className="bar-item">
                      <div className="bar-label">{brand}</div>
                      <div className="bar-container">
                        <div
                          className="bar-fill"
                          style={{
                            width: `${(count / Math.max(...Object.values(byBrand.vehiclesByBrand))) * 100}%`
                          }}
                        >
                          <span className="bar-value">{count}</span>
                        </div>
                      </div>
                    </div>
                  ))}
              </div>
            ) : (
              <div className="no-data">Nenhum dado disponível</div>
            )}
          </div>
        </div>
      </div>

      {lastWeek?.vehicles && lastWeek.vehicles.length > 0 && (
        <div className="recent-vehicles">
          <h2>Veículos Cadastrados esta Semana</h2>
          <div className="vehicles-grid">
            {lastWeek.vehicles.map(vehicle => (
              <div key={vehicle.id} className="vehicle-card">
                <h3>{vehicle.veiculo}</h3>
                <p><strong>Marca:</strong> {vehicle.marca}</p>
                <p><strong>Ano:</strong> {vehicle.ano}</p>
                <p><strong>Cor:</strong> {vehicle.cor || '-'}</p>
                <span className={`badge ${vehicle.vendido ? 'sold' : 'available'}`}>
                  {vehicle.vendido ? 'Vendido' : 'Disponível'}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default Statistics;
