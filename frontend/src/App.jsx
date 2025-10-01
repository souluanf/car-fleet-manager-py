import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import VehicleList from './pages/VehicleList.jsx';
import VehicleForm from './pages/VehicleForm.jsx';
import Statistics from './pages/Statistics.jsx';
import Exercises from './pages/Exercises.jsx';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <nav className="navbar">
          <div className="nav-container">
            <Link to="/" className="nav-logo">
              🚗 Car Fleet Manager
            </Link>
            <ul className="nav-menu">
              <li className="nav-item">
                <Link to="/vehicles" className="nav-link">
                  Veículos
                </Link>
              </li>
              <li className="nav-item">
                <Link to="/statistics" className="nav-link">
                  Estatísticas
                </Link>
              </li>
              <li className="nav-item">
                <Link to="/exercises" className="nav-link">
                  Exercícios
                </Link>
              </li>
            </ul>
          </div>
        </nav>

        <main className="main-content">
          <Routes>
            <Route path="/" element={<Navigate to="/vehicles" replace />} />
            <Route path="/vehicles" element={<VehicleList />} />
            <Route path="/vehicles/new" element={<VehicleForm />} />
            <Route path="/vehicles/edit/:id" element={<VehicleForm />} />
            <Route path="/statistics" element={<Statistics />} />
            <Route path="/exercises" element={<Exercises />} />
          </Routes>
        </main>

        <footer className="footer">
          <p>© 2025 Car Fleet API - Desenvolvido com React</p>
        </footer>
      </div>
    </Router>
  );
}

export default App;
