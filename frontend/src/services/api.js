import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/car-fleet-manager';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const vehicleService = {
  getAll: () => api.get('/api/v1/vehicles'),
  getById: (id) => api.get(`/api/v1/vehicles/${id}`),
  create: (data) => api.post('/api/v1/vehicles', data),
  update: (id, data) => api.put(`/api/v1/vehicles/${id}`, data),
  partialUpdate: (id, data) => api.patch(`/api/v1/vehicles/${id}`, data),
  delete: (id) => api.delete(`/api/v1/vehicles/${id}`),
  search: (params) => api.get('/api/v1/vehicles/search', { params }),
  getStatistics: () => api.get('/api/v1/vehicles/statistics'),
  getByDecade: () => api.get('/api/v1/vehicles/statistics/by-decade'),
  getByBrand: () => api.get('/api/v1/vehicles/statistics/by-brand'),
  getLastWeek: () => api.get('/api/v1/vehicles/statistics/last-week'),
};

export const brandService = {
  getAll: () => api.get('/api/v1/vehicles/brands'),
};

export const exerciseService = {
  calculateVoting: (data) => api.post('/api/v1/exercises/voting', data),
  calculateMultiples: (data) => api.post('/api/v1/exercises/multiplos', data),
  calculateFactorial: (data) => api.post('/api/v1/exercises/fatorial', data),
  bubbleSort: (data) => api.post('/api/v1/exercises/bubble-sort', data),
};

export default api;
