import axios from 'axios';

const API_URL = 'http://localhost:8080/generate-document';

// Interface para os dados que enviaremos para a API
export interface GenerateDocumentPayload {
  templateId: number;
  personId: number;
}

/**
 * Pede ao back-end para gerar um documento a partir de um modelo e uma pessoa.
 * @param payload Objeto contendo o templateId e o personId.
 * @returns O conteúdo HTML final com as variáveis substituídas.
 */
export const generateDocument = async (payload: GenerateDocumentPayload): Promise<string> => {
  try {
    const response = await axios.post<string>(API_URL, payload);
    return response.data;
  } catch (error) {
    console.error("Erro ao gerar documento a partir do modelo:", error);
    throw error;
  }
};