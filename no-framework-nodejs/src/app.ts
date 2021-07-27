import 'reflect-metadata';

import express from 'express';
import {Container} from "typedi";
import {InstructorController} from "./controller/instructor/InstructorController";
import bodyParser from "body-parser";
import {LectureController} from "./controller/lecture/LectureController";

const app = express();
const port = 3000;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

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

app.get('/api/v1/lectures', async (req, res) => {
    const response = await Container.get(LectureController).getLectures(req.params);
    res.send(response);
})

app.get('/api/v1/lecture', async (req, res) => {
    const response = await Container.get(LectureController).getLecture(req.params);
    res.send(response);
})

app.post('/api/v1/lecture/register', async (req, res) => {
    const response = await Container.get(LectureController).register(req.body);
    res.send(response);
})
