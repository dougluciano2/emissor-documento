import axios from "axios";

const API_URL = 'http://localhost:8080/documents/upload';

export interface DocumentUploadPayload{
    title: string;
    htmlContent: String;
}

export interface DocumentResponse{
  id: number;
  title: string;
  fileType: string;
  storagePath: string;
  createdAt: string;
}

/**
* Envia um novo documento para ser salvo no back-end.
* @param payload O objeto contendo o título e o conteúdo HTML.
*/
export const uploadDocument = async (payload: DocumentUploadPayload): Promise<DocumentResponse> => {
  try {
    const response = await axios.post<DocumentResponse>(`${API_URL}`, payload);
    return response.data;
  } catch (error) {
    console.error("Erro ao fazer upload do documento:", error);
    throw error;
  }
};