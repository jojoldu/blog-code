import 'reflect-metadata';

import express from 'express';
import mysql from 'mysql';
import {Container} from "typedi";
import {InstructorController} from "./controller/instructor/InstructorController";
import bodyParser from "body-parser";

const app = express();
const port = 3000;

app.use(bodyParser.json());

app.listen(port, () => {
    console.info(`Server is running on http://localhost:${port}`);
});

app.get('/', (req, res) => {
    const response = Container.get(InstructorController).getInstructors();
    res.send(response);
})

app.get('/now', async (req, res) => {
    const response = await Container.get(InstructorController).getNow();
    res.send(response);
})
