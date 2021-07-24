import 'reflect-metadata';

import express from 'express';
import mysql from 'mysql';
import {Container} from "typedi";
import {InstructorController} from "./controller/instructor/InstructorController";

const app = express();
const port = 3000;

app.listen(port, () => {
    console.info(`Server is running on http://localhost:${port}`);
});

app.get('/', (req, res) => {
    const response = Container.get(InstructorController).getInstructors();
    res.send(response);
})
