import axios from "axios";

const API_URL = 'http://localhost:8080/documents';


export interface DocumentUploadPayload{
    title: string;
    htmlContent: string;
    personId: number;
}

export interface DocumentResponse{
  id: number;
  title: string;
  fileType: string;
  storagePath: string;
  createdAt: string;
  medicalRecordIndex: number;
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
    alert('Erro ao fazer upload do documento')
    console.error("Erro ao fazer upload do documento:", error);
    throw error;
  }
};

export const getDocumentContent = async (documentId: number): Promise<string> => {

  try{
    const response = await axios.get<string>(`${API_URL}/${documentId}/download`);
    return response.data;
  } catch(error){
    alert('Erro ao baixar documentos!');
    console.error(`Erro ao buscar conteúdo da pasta digital para o id ${documentId}/download`);
    throw error;
  }

}