import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Editor } from '@tinymce/tinymce-react';
import { Button, Form, Modal } from 'react-bootstrap';
import { createTemplate } from '../services/template.service';

export default function TemplateEditorPage() {
    const editorRef = useRef<any>(null);
    const navigate = useNavigate();

    // states para gerenciamento do modal
    const [showCancelModal, setShowCancelModal] = useState(false);
    const handleCloseCancelModal = () => setShowCancelModal(false);
    const handleShowCancelModal = () => setShowCancelModal(true);

    const handleConfirmCancel = () => {
        handleCloseCancelModal(); // Fecha o modal
        navigate('/'); // Navega para a página principal
    };

    // Estado para guardar o nome do modelo
    const [templateName, setTemplateName] = useState('');

    const handleSave = async () => {
        if (editorRef.current && templateName) {
            const content = editorRef.current.getContent();

            if (!content) {
                alert('O conteúdo do modelo está vazio!');
                return;
            }

            // Prepara os dados para enviar para a API de templates
            const payload = {
                name: templateName,
                htmlContent: content
            };

            try {
                await createTemplate(payload); // Chama a função correta do serviço de template
                alert('Modelo salvo com sucesso!');
                navigate('/'); // Volta para a página principal após salvar
            } catch (error) {
                alert('Ocorreu um erro ao salvar o modelo. Veja o console.');
                console.error('Falha ao criar modelo:', error);
            }
        } else {
            alert('Por favor, preencha o nome do modelo.');
        }
    };

    return (
        <div className="container mt-4">
            <h2>Criar / Editar Modelo de Documento</h2>
            <p>Crie o conteúdo base para seus documentos. Use placeholders como [NomeDoPaciente] que serão substituídos depois.</p>

            {/* Campo para o nome do modelo */}
            <Form.Group className="mb-3">
                <Form.Label><strong>Nome do Modelo</strong></Form.Label>
                <Form.Control
                    type="text"
                    placeholder="Ex: Atestado de Comparecimento Padrão"
                    value={templateName}
                    onChange={(e) => setTemplateName(e.target.value)}
                />
            </Form.Group>

            <Editor
                apiKey="2p1hg8ixonxzcuh3622m7z7cpaig1md6n49e82uv3mzmr334"
                onInit={(evt, editor) => editorRef.current = editor}
                initialValue=""
                init={{
                    width: 800,
                    height: 500,
                    menubar: true,
                    plugins: 'advlist autolink lists link image charmap preview anchor searchreplace visualblocks code fullscreen insertdatetime media table help wordcount',
                    toolbar: 'undo redo | blocks | ' +
                        'bold italic forecolor | alignleft aligncenter ' +
                        'alignright alignjustify | bullist numlist outdent indent | ' +
                        'removeformat | help',
                }}
            />
            <div className="mt-3">
                <Button variant="primary" onClick={handleSave}>
                    Salvar Modelo
                </Button>
                <Button variant="secondary" onClick={handleShowCancelModal} className="ms-2">
                    Cancelar
                </Button>
            </div>

            {/* componente do modal */}
            <Modal show={showCancelModal} onHide={handleCloseCancelModal} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Confirmar Cancelamento</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Tem certeza que deseja cancelar?
                    Qualquer alteração não salva no modelo será perdida.
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseCancelModal}>
                        Retornar ao Editor
                    </Button>
                    <Button variant="danger" onClick={handleConfirmCancel}>
                        Sim, Cancelar
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}