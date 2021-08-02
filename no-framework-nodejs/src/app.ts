import 'reflect-metadata';

import express from 'express';
import {Container} from "typedi";
import {InstructorController} from "./controller/instructor/InstructorController";
import bodyParser from "body-parser";
import {LectureController} from "./controller/lecture/LectureController";
import {NumberUtil} from "./util/NumberUtil";
import {StudentController} from "./controller/student/StudentController";

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

// lecture
app.get('/api/v1/lectures', async (req, res) => {
    const response = await Container.get(LectureController).getLectures(req.params);
    res.send(response);
})

app.get('/api/v1/lecture', async (req, res) => {
    const response = await Container.get(LectureController).getLecture(req.params);
    res.send(response);
})

app.post('/api/v1/lecture', async (req, res) => {
    const response = await Container.get(LectureController).create(req.body);
    res.send(response);
})

app.patch('/api/v1/lecture/:lectureId/update', async (req, res) => {
    const lectureId = NumberUtil.toNumber(req.params.lectureId);
    const response = await Container.get(LectureController).update(lectureId, req.body);
    res.send(response);
})

app.patch('/api/v1/lecture/:lectureId/register', async (req, res) => {
    const lectureId = NumberUtil.toNumber(req.params.lectureId);
    const response = await Container.get(LectureController).register(lectureId, req.body);
    res.send(response);
})

app.patch('/api/v1/lecture/:lectureId/publish', async (req, res) => {
    const lectureId = NumberUtil.toNumber(req.params.lectureId);
    const response = await Container.get(LectureController).publish(lectureId);
    res.send(response);
})

// student
app.post('/api/v1/student/signup', async (req, res) => {
    const response = await Container.get(StudentController).signup(req.body);
    res.send(response);
})

// instructor
app.post('/api/v1/instructor/signup', async (req, res) => {
    const response = await Container.get(InstructorController).signup(req.body);
    res.send(response);
})
