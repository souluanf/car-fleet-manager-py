import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { vehicleService, brandService } from '../services/api';
import '../styles/VehicleForm.css';

function VehicleForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const [brands, setBrands] = useState([]);
  const [formData, setFormData] = useState({
    veiculo: '',
    marca: '',
    ano: new Date().getFullYear(),
    descricao: '',
    cor: '',
    vendido: false
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadBrands();
    if (isEdit) {
      loadVehicle();
    }
  }, [id]);

  const loadBrands = async () => {
    try {
      const response = await brandService.getAll();
      setBrands(response.data);
    } catch (err) {
      console.error('Erro ao carregar marcas:', err);
    }
  };

  const loadVehicle = async () => {
    try {
      setLoading(true);
      const response = await vehicleService.getById(id);
      setFormData(response.data);
    } catch (err) {
      alert('Erro ao carregar veículo');
      navigate('/vehicles');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.veiculo.trim()) {
      newErrors.veiculo = 'Nome do veículo é obrigatório';
    }
    if (!formData.marca.trim()) {
      newErrors.marca = 'Marca é obrigatória';
    }
    if (!formData.ano || formData.ano < 1900) {
      newErrors.ano = 'Ano inválido';
    }
    if (!formData.descricao.trim()) {
      newErrors.descricao = 'Descrição é obrigatória';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) {
      return;
    }

    try {
      setLoading(true);
      if (isEdit) {
        await vehicleService.update(id, formData);
      } else {
        await vehicleService.create(formData);
      }
      navigate('/vehicles');
    } catch (err) {
      if (err.response?.data?.message) {
        alert(err.response.data.message);
      } else {
        alert('Erro ao salvar veículo');
      }
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEdit) {
    return <div className="loading">Carregando...</div>;
  }

  return (
    <div className="vehicle-form-container">
      <div className="form-card">
        <h1>{isEdit ? 'Editar Veículo' : 'Novo Veículo'}</h1>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="veiculo">Nome do Veículo *</label>
            <input
              type="text"
              id="veiculo"
              name="veiculo"
              value={formData.veiculo}
              onChange={handleChange}
              className={errors.veiculo ? 'error' : ''}
              maxLength={100}
            />
            {errors.veiculo && <span className="error-message">{errors.veiculo}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="marca">Marca *</label>
            <select
              id="marca"
              name="marca"
              value={formData.marca}
              onChange={handleChange}
              className={errors.marca ? 'error' : ''}
            >
              <option value="">Selecione uma marca</option>
              {brands.map(brand => (
                <option key={brand.name} value={brand.name}>
                  {brand.name}
                </option>
              ))}
            </select>
            {errors.marca && <span className="error-message">{errors.marca}</span>}
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="ano">Ano *</label>
              <input
                type="number"
                id="ano"
                name="ano"
                value={formData.ano}
                onChange={handleChange}
                className={errors.ano ? 'error' : ''}
                min={1900}
                max={new Date().getFullYear() + 1}
              />
              {errors.ano && <span className="error-message">{errors.ano}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="cor">Cor</label>
              <input
                type="text"
                id="cor"
                name="cor"
                value={formData.cor}
                onChange={handleChange}
                maxLength={50}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="descricao">Descrição *</label>
            <textarea
              id="descricao"
              name="descricao"
              value={formData.descricao}
              onChange={handleChange}
              className={errors.descricao ? 'error' : ''}
              rows={4}
              maxLength={1000}
            />
            {errors.descricao && <span className="error-message">{errors.descricao}</span>}
          </div>

          <div className="form-group checkbox-group">
            <label>
              <input
                type="checkbox"
                name="vendido"
                checked={formData.vendido}
                onChange={handleChange}
              />
              <span>Veículo vendido</span>
            </label>
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="btn btn-outline"
              onClick={() => navigate('/vehicles')}
              disabled={loading}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Salvando...' : 'Salvar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default VehicleForm;
