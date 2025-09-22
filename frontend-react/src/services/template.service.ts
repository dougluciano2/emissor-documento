import axios from "axios";


const API_URL = 'http://localhost:8080/templates';

export interface Template{
    id: number;
    name: string;
    createdAt: string;
    updatedAt: string;
}

export interface TemplatePayload {
  name: string;
  htmlContent: string;
}

/**
 * Busca a lista de todos os modelos de documento disponíveis.
 */
export const getTemplates = async (): Promise<Template[]> => {
  try {
    const response = await axios.get<Template[]>(API_URL);
    return response.data;
  } catch (error) {
    alert('Erro ao buscar modelos');
    console.error("Erro ao buscar modelos:", error);
    throw error;
  }
};

/**
 * Busca o conteúdo HTML de um modelo específico.
 * @param templateId O ID do modelo.
 */
export const getTemplateContent = async (templateId: number): Promise<string> => {
  try {
    const response = await axios.get<string>(`${API_URL}/${templateId}/content`);
    return response.data;
  } catch (error) {
    alert('Erro ao carregar o conteúdo do modelo');
    console.error(`Erro ao carregar o conteúdo do modelo ID: ${templateId}:`, error);
    throw error;
  }
};

export const createTemplate = async (payload: TemplatePayload): Promise<Template> => {
  try {
    const response = await axios.post<Template>(API_URL, payload);
    return response.data;
  } catch (error) {
    console.error("Erro ao criar o modelo:", error);
    throw error;
  }
};