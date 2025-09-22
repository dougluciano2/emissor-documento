import { useState, useEffect, type ChangeEvent, type FormEvent } from 'react';
import { findAll, createPerson, type Person, type NewPerson } from '../services/person.service';
import { useNavigate } from 'react-router-dom';
import TemplateSelectModal from '../components/TemplateSelectModal';


export function PersonManagementPage() {
  // Estado para a lista de pessoas
  const [people, setPeople] = useState<Person[]>([]);
  const navigate = useNavigate();

  // estados para controlar o modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedPersonId, setSelectedPersonId] = useState<number | null>(null);



  // Estado para os dados do formulário de nova pessoa
  const [newPerson, setNewPerson] = useState<NewPerson>({
    name: '',
    cpf: '',
    address: '',
    medicalDiagnostics: ''
  });

  // Efeito para carregar a lista de pessoas (apenas uma vez)
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

  // Handlers para os botões de ação (placeholders)
  const handleEdit = (personId: number) => {
    console.log("Ação: Editar pessoa com ID:", personId);
  };

  const handleEmitDocument = (personId: number) => {
    console.log("Ação: Emitir documento para a pessoa com ID:", personId);
    setSelectedPersonId(personId);
    setIsModalOpen(true);
  };

  const handleViewRecord = (personId: number) => {
    console.log("Ação: Visualizar prontuário da pessoa com ID:", personId);
    navigate(`/medical-record/${personId}`);
  };

  //handle para a função de confirmação do modal
  const handleModalConfirm = (templateId: string) => {
    setIsModalOpen(false)
    if (selectedPersonId) {
      navigate(`/document/editor/${selectedPersonId}?templateId=${templateId}`);
    }
  }

  // Handler para atualizar o estado do formulário enquanto o usuário digita
  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setNewPerson(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  // Handler para submeter o formulário
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault(); // Previne o recarregamento da página
    try {
      const createdPerson = await createPerson(newPerson);
      // Adiciona a pessoa recém-criada (o objeto) à lista na tela
      setPeople(prevPeople => [...prevPeople, createdPerson]);
      // Limpa os campos do formulário
      setNewPerson({ name: '', cpf: '', address: '', medicalDiagnostics: '' });
      alert("Pessoa criada com sucesso!");
    } catch (error) {
      console.error("Falha ao criar pessoa:", error);
      alert("Falha ao criar pessoa!");
    }
  };

  // Um único return para todo o componente
  return (
    <div className="container mt-4">

      {/* Seção do Formulário de Cadastro */}
      <div className="mb-5">
        <h2>Cadastrar Nova Pessoa</h2>
        <form onSubmit={handleSubmit}> {/* Nome da função corrigido */}
          {/* Inputs do formulário lendo do estado 'newPerson' */}
          <div className="mb-3">
            <label htmlFor="name" className="form-label">Nome</label>
            <input
              type="text"
              className="form-control"
              id="name"
              name="name"
              value={newPerson.name}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="cpf" className="form-label">CPF</label>
            <input
              type="text"
              className="form-control"
              id="cpf"
              name="cpf"
              value={newPerson.cpf}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="address" className="form-label">Endereço</label>
            <input
              type="text"
              className="form-control"
              id="address"
              name="address"
              value={newPerson.address}
              onChange={handleInputChange}
            />
          </div>
          <div className="mb-3">
            <label htmlFor="medicalDiagnostics" className="form-label">Diagnóstico Médico</label>
            <input
              type="text"
              className="form-control"
              id="medicalDiagnostics"
              name="medicalDiagnostics"
              value={newPerson.medicalDiagnostics}
              onChange={handleInputChange}
            />
          </div>
          <button type="submit" className="btn btn-success">Salvar Pessoa</button>
        </form>
      </div>

      <hr />

      {/* Seção da Listagem de Pessoas */}
      <h2 className="mb-3">Pessoas Cadastradas</h2>
      {people.length === 0 && <p>Nenhuma pessoa cadastrada.</p>}

      {people.length > 0 && (
        <table className="table table-striped table-hover">
          <thead className="table-dark">
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>CPF</th>
              <th>Endereço</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {people.map((person) => (
              <tr key={person.id}>
                <td>{person.id}</td>
                <td>{person.name}</td>
                <td>{person.cpf}</td>
                <td>{person.address}</td>
                <td>
                  <button className="btn btn-primary btn-sm me-2" onClick={() => handleEdit(person.id)}>Editar</button>
                  <button className="btn btn-secondary btn-sm me-2" onClick={() => handleEmitDocument(person.id)}>Emitir Documento</button>
                  <button className="btn btn-info btn-sm text-white" onClick={() => handleViewRecord(person.id)}>Prontuário</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <div className="container mt-4">
        {/* ... seu formulário e tabela JSX continuam iguais ... */}

        {/* 5. ADICIONE O COMPONENTE DO MODAL AO FINAL DO SEU JSX */}
        <TemplateSelectModal
          show={isModalOpen}
          personId={selectedPersonId}
          onClose={() => setIsModalOpen(false)}
          onConfirm={handleModalConfirm}
        />
      </div>

    </div>
  );
}

export default PersonManagementPage;