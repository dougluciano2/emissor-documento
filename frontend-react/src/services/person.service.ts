import axios from "axios";

const API_URL = 'http://localhost:8080/persons'

export interface Person {
    id: number;
    name: string;
    cpf: string;
    address: string;
    medicalDiagnostics: string;
    createdAt: string;
    updatedAt: string;
}

export type NewPerson = Omit<Person, 'id' | 'createdAt' | 'updatedAt'>;

export const findAll = async(): Promise<Person[]> => {
    try{

        const response = await axios.get<Person[]>(API_URL);
        return response.data;

    } catch (error) {
        console.error("Erro ao buscar pessoas: ", error);
        throw error;
    }
}