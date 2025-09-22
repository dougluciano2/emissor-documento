import { useEffect, useRef, useState } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { Editor } from "@tinymce/tinymce-react";
import { Button, Modal } from "react-bootstrap";
import { uploadDocument } from "../services/document.service";

import { generateDocument } from '../services/template-generation.service';


export function DocumentEditorPage() {

  const { personId } = useParams();
  const editorRef = useRef<any>(null);
  const navigate = useNavigate();
  // Hook para ler os parâmetros da URL, como ?templateId=1
  const [searchParams] = useSearchParams();

  // Estado para guardar o conteúdo inicial do editor
  const [initialEditorContent, setInitialEditorContent] = useState<string>('<p>Carregando...</p>');
  const [showCancelModal, setShowCancelModal] = useState(false);
  const handleCloseCancelModal = () => setShowCancelModal(false);
  const handleShowCancelModal = () => setShowCancelModal(true);

  const handleConfirmCancel = () => {
    handleCloseCancelModal();
    navigate('/');
  }

  useEffect(() => {
    const templateId = searchParams.get('templateId');
    const numericPersonId = Number(personId);

    const loadInitialContent = async () => {
      // Se não tiver um ID de pessoa, não faz nada
      if (!numericPersonId) return;

      if (templateId && templateId !== 'blank') {
        // Se um modelo foi selecionado, chama o serviço de GERAÇÃO
        try {
          const numericTemplateId = Number(templateId);
          const payload = {
            templateId: numericTemplateId,
            personId: numericPersonId
          };
          // Chama a nova função!
          const content = await generateDocument(payload);
          setInitialEditorContent(content);
        } catch (error) {
          console.error("Erro ao gerar documento:", error);
          setInitialEditorContent('<p class="text-danger">Erro ao gerar o documento a partir do modelo.</p>');
        }
      } else {
        // Se for 'blank' ou não tiver ID, começa com um parágrafo vazio
        setInitialEditorContent('<p>Escreva seu documento aqui.</p>');
      }
    };

    loadInitialContent();
  }, [personId, searchParams]);

  const handleSave = async () => {
    if (editorRef.current) {
      const content = editorRef.current.getContent();

      if (!content) {
        alert("Documento vazio!");
        return;
      }

      const payload = {
        title: `Documento para pessoa ID ${personId} - ${new Date().toLocaleDateString()}`,
        htmlContent: content,
        personId: Number(personId)
      }

      console.log("Enviando este payload para a API: ", payload)

      try {
        await uploadDocument(payload);
        alert('Documento salvo com sucesso!');
        navigate('/');
      } catch (error) {
        alert('Ocorreu um erro ao salvar o documento. Veja o console para mais detalhes.');
        console.error('Falha no upload: ', error)
      }
    }


  };


  return (
    <div className="container mt-4">
      {/* Mostra para qual pessoa estamos emitindo o documento */}
      <h2>Emitindo Documento para a Pessoa ID: {personId}</h2>

      {/* Componente do Editor TinyMCE */}
      <Editor
        key={initialEditorContent}
        apiKey="2p1hg8ixonxzcuh3622m7z7cpaig1md6n49e82uv3mzmr334" // <-- COLE SUA CHAVE DE API AQUI!
        onInit={(evt, editor) => editorRef.current = editor}
        initialValue={initialEditorContent}
        init={{
          width: 800,
          height: 600,
          menubar: true,
          plugins: [
            'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
            'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
            'insertdatetime', 'media', 'table', 'help', 'wordcount'
          ],
          toolbar: 'undo redo | blocks | ' +
            'bold italic forecolor | alignleft aligncenter ' +
            'alignright alignjustify | bullist numlist outdent indent | ' +
            'removeformat | help',
          content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }'
        }}
      />
      <div className="mt-3">
        <Button variant="success" onClick={handleSave}>
          Salvar Documento
        </Button>
        <Button variant="secondary" onClick={handleShowCancelModal} className="ms-2">
          Cancelar Emissão
        </Button>
      </div>

      <Modal show={showCancelModal} onHide={handleCloseCancelModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Confirmar Cancelamento</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Tem certeza que deseja cancelar a emissão do documento?
          Essa ação não salvará nenhum conteúdo não salvo.
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseCancelModal}>
            Retornar ao editor
          </Button>
          <Button variant="danger" onClick={handleConfirmCancel}>
            Sim, cancelar
          </Button>
        </Modal.Footer>
      </Modal>


    </div>


  );
}