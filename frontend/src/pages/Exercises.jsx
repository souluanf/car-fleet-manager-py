import React, { useState } from 'react';
import { exerciseService } from '../services/api';
import '../styles/Exercises.css';

function Exercises() {
  const [activeTab, setActiveTab] = useState('voting');
  const [results, setResults] = useState({});
  const [loading, setLoading] = useState(false);

  const [votingData, setVotingData] = useState({
    totalEleitores: '',
    votosValidos: '',
    votosBrancos: '',
    votosNulos: ''
  });

  const [multipleNumber, setMultipleNumber] = useState('');
  const [factorialNumber, setFactorialNumber] = useState('');
  const [arrayInput, setArrayInput] = useState('');

  const handleVotingSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await exerciseService.calculateVoting({
        totalEleitores: parseInt(votingData.totalEleitores),
        votosValidos: parseInt(votingData.votosValidos),
        votosBrancos: parseInt(votingData.votosBrancos),
        votosNulos: parseInt(votingData.votosNulos)
      });
      setResults({ ...results, voting: response.data, votingError: null });
    } catch (err) {
      const errorMessage = err.response?.data?.detail || err.response?.data?.message || 'Erro ao calcular percentuais';
      setResults({ ...results, voting: null, votingError: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  const handleMultiplesSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await exerciseService.calculateMultiples({
        numero: parseInt(multipleNumber)
      });
      setResults({ ...results, multiples: response.data, multiplesError: null });
    } catch (err) {
      const errorMessage = err.response?.data?.detail || err.response?.data?.message || 'Erro ao calcular múltiplos';
      setResults({ ...results, multiples: null, multiplesError: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  const handleFactorialSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await exerciseService.calculateFactorial({
        numero: parseInt(factorialNumber)
      });
      setResults({ ...results, factorial: response.data, factorialError: null });
    } catch (err) {
      const errorMessage = err.response?.data?.detail || err.response?.data?.message || 'Erro ao calcular fatorial';
      setResults({ ...results, factorial: null, factorialError: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  const handleBubbleSortSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const arrayNumbers = arrayInput.split(',').map(n => parseInt(n.trim())).filter(n => !isNaN(n));
      const response = await exerciseService.bubbleSort({
        vetor: arrayNumbers
      });
      setResults({ ...results, bubbleSort: response.data, bubbleSortError: null });
    } catch (err) {
      const errorMessage = err.response?.data?.detail || err.response?.data?.message || 'Erro ao ordenar vetor';
      setResults({ ...results, bubbleSort: null, bubbleSortError: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="exercises-container">
      <h1>Exercícios de Lógica</h1>

      <div className="tabs">
        <button
          className={`tab ${activeTab === 'voting' ? 'active' : ''}`}
          onClick={() => setActiveTab('voting')}
        >
          1. Votos
        </button>
        <button
          className={`tab ${activeTab === 'bubble' ? 'active' : ''}`}
          onClick={() => setActiveTab('bubble')}
        >
          2. Bubble Sort
        </button>
        <button
          className={`tab ${activeTab === 'factorial' ? 'active' : ''}`}
          onClick={() => setActiveTab('factorial')}
        >
          3. Fatorial
        </button>
        <button
          className={`tab ${activeTab === 'multiples' ? 'active' : ''}`}
          onClick={() => setActiveTab('multiples')}
        >
          4. Múltiplos
        </button>
      </div>

      <div className="tab-content">
        {activeTab === 'voting' && (
          <div className="exercise-card">
            <h2>Cálculo de Percentuais de Votos</h2>
            <p className="description">
              Calcula o percentual de votos válidos, brancos e nulos em relação ao total de eleitores.
            </p>
            <form onSubmit={handleVotingSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Total de Eleitores</label>
                  <input
                    type="number"
                    value={votingData.totalEleitores}
                    onChange={(e) => setVotingData({ ...votingData, totalEleitores: e.target.value })}
                    required
                    min="1"
                  />
                </div>
                <div className="form-group">
                  <label>Votos Válidos</label>
                  <input
                    type="number"
                    value={votingData.votosValidos}
                    onChange={(e) => setVotingData({ ...votingData, votosValidos: e.target.value })}
                    required
                    min="0"
                  />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Votos Brancos</label>
                  <input
                    type="number"
                    value={votingData.votosBrancos}
                    onChange={(e) => setVotingData({ ...votingData, votosBrancos: e.target.value })}
                    required
                    min="0"
                  />
                </div>
                <div className="form-group">
                  <label>Votos Nulos</label>
                  <input
                    type="number"
                    value={votingData.votosNulos}
                    onChange={(e) => setVotingData({ ...votingData, votosNulos: e.target.value })}
                    required
                    min="0"
                  />
                </div>
              </div>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Calculando...' : 'Calcular'}
              </button>
            </form>

            {results.voting && (
              <div className="result-card">
                <h3>Resultado:</h3>
                <p>Percentual de Votos Válidos: <strong>{results.voting.percentualVotosValidos.toFixed(2)}%</strong></p>
                <p>Percentual de Votos Brancos: <strong>{results.voting.percentualVotosBrancos.toFixed(2)}%</strong></p>
                <p>Percentual de Votos Nulos: <strong>{results.voting.percentualVotosNulos.toFixed(2)}%</strong></p>
              </div>
            )}

            {results.votingError && (
              <div className="error-card">
                <h3>❌ Erro:</h3>
                <p>{results.votingError}</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'bubble' && (
          <div className="exercise-card">
            <h2>Ordenação com Bubble Sort</h2>
            <p className="description">
              Ordena um vetor de números inteiros utilizando o algoritmo Bubble Sort.
            </p>
            <form onSubmit={handleBubbleSortSubmit}>
              <div className="form-group">
                <label>Digite os números separados por vírgula</label>
                <input
                  type="text"
                  value={arrayInput}
                  onChange={(e) => setArrayInput(e.target.value)}
                  placeholder="Ex: 5, 3, 8, 1, 2"
                  required
                />
              </div>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Ordenando...' : 'Ordenar'}
              </button>
            </form>

            {results.bubbleSort && (
              <div className="result-card">
                <h3>Resultado:</h3>
                <p>Vetor Original: <strong>[{results.bubbleSort.vetorOriginal.join(', ')}]</strong></p>
                <p>Vetor Ordenado: <strong>[{results.bubbleSort.vetorOrdenado.join(', ')}]</strong></p>
              </div>
            )}

            {results.bubbleSortError && (
              <div className="error-card">
                <h3>❌ Erro:</h3>
                <p>{results.bubbleSortError}</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'factorial' && (
          <div className="exercise-card">
            <h2>Cálculo de Fatorial</h2>
            <p className="description">
              Calcula o fatorial de um número natural usando recursão.
            </p>
            <form onSubmit={handleFactorialSubmit}>
              <div className="form-group">
                <label>Digite um número</label>
                <input
                  type="number"
                  value={factorialNumber}
                  onChange={(e) => setFactorialNumber(e.target.value)}
                  min="0"
                  required
                />
              </div>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Calculando...' : 'Calcular'}
              </button>
            </form>

            {results.factorial && (
              <div className="result-card">
                <h3>Resultado:</h3>
                <p>Fatorial de {results.factorial.numero}: <strong>{results.factorial.fatorial}</strong></p>
              </div>
            )}

            {results.factorialError && (
              <div className="error-card">
                <h3>❌ Erro:</h3>
                <p>{results.factorialError}</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'multiples' && (
          <div className="exercise-card">
            <h2>Soma de Múltiplos de 3 ou 5</h2>
            <p className="description">
              Calcula a soma de todos os números múltiplos de 3 ou 5 abaixo de um número X.
            </p>
            <form onSubmit={handleMultiplesSubmit}>
              <div className="form-group">
                <label>Digite um número</label>
                <input
                  type="number"
                  value={multipleNumber}
                  onChange={(e) => setMultipleNumber(e.target.value)}
                  min="1"
                  required
                />
              </div>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Calculando...' : 'Calcular'}
              </button>
            </form>

            {results.multiples && (
              <div className="result-card">
                <h3>Resultado:</h3>
                <p>Soma dos múltiplos de 3 ou 5 abaixo de {results.multiples.numeroLimite}: <strong>{results.multiples.somaMultiplos}</strong></p>
              </div>
            )}

            {results.multiplesError && (
              <div className="error-card">
                <h3>❌ Erro:</h3>
                <p>{results.multiplesError}</p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default Exercises;
