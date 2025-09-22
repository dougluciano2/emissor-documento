import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getDocumentContent, type DocumentResponse } from "../services/document.service";
import { getMedicalRecord } from "../services/medical-record.service";
import './MedicalRecordPage.css'; // Importa nosso arquivo de estilo

export default function MedicalRecordPage() {
  const { personId } = useParams();

  const [documentIndex, setDocumentIndex] = useState<DocumentResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const [selectedDocumentId, setSelectedDocumentId] = useState<number | null>(null);
  const [selectedDocumentContent, setSelectedDocumentContent] = useState<string>('');
  const [isContentLoading, setIsContentLoading] = useState(false);

  // Efeito para carregar o índice de documentos quando a página abre
  useEffect(() => {
    const numericPersonId = Number(personId);
    if (numericPersonId) {
      const loadRecord = async () => {
        try {
          setIsLoading(true);
          const data = await getMedicalRecord(numericPersonId);
          console.log("Dados recebidos da API:", data); 
          setDocumentIndex(data);
        } catch (error) {
          console.error('Falha ao carregar o prontuário! ', error);
        } finally {
          setIsLoading(false);
        }
      };
      loadRecord();
    }
  }, [personId]);

  // Função auxiliar para formatar a data para o padrão pt-BR
  const formatDate = (dateString: string) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  };

  // Função para buscar e exibir o conteúdo de um documento selecionado
  const handleSelectDocument = async (documentId: number) => {
    setSelectedDocumentId(documentId);
    setIsContentLoading(true);
    setSelectedDocumentContent(''); // Limpa o conteúdo anterior

    try {
      const content = await getDocumentContent(documentId);
      setSelectedDocumentContent(content);
    } catch (error) {
      setSelectedDocumentContent('<p class="text-danger">Erro ao carregar o documento.</p>');
    } finally {
      setIsContentLoading(false);
    }
  };

  return (
    <div className="container-fluid mt-4"> {/* Usei container-fluid para mais espaço */}
      <h1>Prontuário Digital</h1>
      <p>Exibindo prontuário para a Pessoa com ID: <strong>{personId}</strong></p>
      <hr />

      <div className="row">
        {/* Coluna do Índice (Esquerda) */}
        <div className="col-md-4">
          <h3>Índice de Documentos</h3>
          {isLoading && <p>Carregando índice...</p>}
          
          {!isLoading && documentIndex.length === 0 && (
            <div className="alert alert-warning">Nenhum documento encontrado para esta pessoa.</div>
          )}

          {documentIndex.length > 0 && (
            <div className="list-group">
              {documentIndex.map((doc) => (
                <button
                  type="button"
                  key={doc.id}
                  className={`list-group-item list-group-item-action text-start ${selectedDocumentId === doc.id ? 'active' : ''}`}
                  onClick={() => handleSelectDocument(doc.id)}
                >
                  <div className="d-flex w-100 justify-content-between">
                    <h5 className="mb-1">Página {doc.medicalRecordIndex}</h5>
                    <small>{formatDate(doc.createdAt)}</small>
                  </div>
                  <p className="mb-1">{doc.title}</p>
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Coluna do Conteúdo (Direita) */}
        <div className="col-md-8">
          <h3>Conteúdo do Documento</h3>
          {/* O div abaixo agora usa a classe CSS para simular uma folha A4 */}
          <div className="document-viewer-a4 p-4"> 
            {isContentLoading && <p>Carregando documento...</p>}
            
            {!isContentLoading && selectedDocumentContent && (
              <div dangerouslySetInnerHTML={{ __html: selectedDocumentContent }} />
            )}

            {!isContentLoading && !selectedDocumentContent && (
              <p className="text-muted">Selecione um documento no índice para visualizar o conteúdo.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}