import { useState, useEffect } from 'react';
import { findAll, type Person } from './services/person.service';
// Não precisamos mais do nosso App.css por enquanto
// import './App.css'; 

function App() {
  const [people, setPeople] = useState<Person[]>([]);

  useEffect(() => {
    const loadPeople = async () => {
      try {
        const data = await findAll();
        setPeople(data);
      } catch (error) {
        console.error("Falha ao carregar pessoas no componente:", error);
      }
    };
    loadPeople();
  }, []);

  return (
    // 'container' e 'mt-4' (margin-top 4) são classes do Bootstrap
    <div className="container mt-4"> 
      <h1 className="mb-3">Pessoas Cadastradas</h1> {/* mb-3 adiciona uma margem inferior */}

      {people.length === 0 && <p>Carregando pessoas...</p>}
      
      {people.length > 0 && (
        // Classes de tabela do Bootstrap
        <table className="table table-striped table-hover">
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>CPF</th>
              <th>Endereço</th>
            </tr>
          </thead>
          <tbody>
            {people.map((person) => (
              <tr key={person.id}>
                <td>{person.id}</td>
                <td>{person.name}</td>
                <td>{person.cpf}</td>
                <td>{person.address}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default App;