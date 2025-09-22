import axios from "axios";

import { type DocumentResponse } from "./document.service";

const API_URL = 'http://localhost:8080/documents';

export const getMedicalRecord = 
    async (personId: number): Promise<DocumentResponse[]> => {

        try{
            const response = await axios.get<DocumentResponse[]>(API_URL, {
                params: {personId: personId}
            });
            return response.data;
        } catch(error){
            alert("Erro ao abrir o prontuário digital!")
            console.error(`Erro ao abrir o prontuário digital para a pessoa ${personId}! `, error);
            throw error;
        };
    }