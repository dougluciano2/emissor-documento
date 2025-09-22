import { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { getTemplates, type Template } from '../services/template.service';

// Define a "forma" das propriedades que nosso modal espera receber
interface TemplateSelectModalProps {
  show: boolean;
  personId: number | null;
  onClose: () => void;
  onConfirm: (selectedTemplateId: string) => void;
}

export default function TemplateSelectModal({ show, personId, onClose, onConfirm }: TemplateSelectModalProps) {
  const [templates, setTemplates] = useState<Template[]>([]);
  const [selectedTemplate, setSelectedTemplate] = useState<string>(''); // Guarda o ID do template escolhido

  // Efeito que busca a lista de templates da API quando o modal abre
  useEffect(() => {
    // Só busca os templates se o modal estiver visível
    if (show) {
      const loadTemplates = async () => {
        try {
          const data = await getTemplates();
          setTemplates(data);
        } catch (error) {
          console.error("Erro ao carregar modelos:", error);
          alert("Não foi possível carregar os modelos de documento.");
        }
      };
      loadTemplates();
    }
  }, [show]); // Roda o efeito toda vez que a propriedade 'show' mudar

  const handleConfirmClick = () => {
    if (!selectedTemplate) {
      alert("Por favor, selecione uma opção.");
      return;
    }
    onConfirm(selectedTemplate);
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Emitir Documento para Pessoa ID: {personId}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>Selecione um modelo de documento ou comece com um documento em branco.</p>
        <Form.Select 
          aria-label="Seleção de Modelo"
          value={selectedTemplate}
          onChange={(e) => setSelectedTemplate(e.target.value)}
        >
          <option value="">Selecione uma opção...</option>
          <option value="blank">Começar com Documento em Branco</option>
          {templates.map((template) => (
            <option key={template.id} value={template.id}>
              {template.name}
            </option>
          ))}
        </Form.Select>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Cancelar
        </Button>
        <Button variant="primary" onClick={handleConfirmClick}>
          Confirmar e Abrir Editor
        </Button>
      </Modal.Footer>
    </Modal>
  );
}