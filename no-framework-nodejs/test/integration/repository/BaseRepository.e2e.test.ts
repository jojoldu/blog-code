import "reflect-metadata"

import {Container} from "typedi";
import {NodePgTemplate} from "../../../src/config/database/NodePgTemplate";
import {TestSupportRepository} from "../TestSupportRepository";
import {TestLectureCreator} from "../TestLectureCreator";
import {BaseRepository} from "../../../src/repository/BaseRepository";
import {Lecture} from "../../../src/entity/lecture/Lecture";

describe('BaseRepository', () => {
    const nodePgTemplate: NodePgTemplate = Container.get(NodePgTemplate);
    const testSupportRepository: TestSupportRepository = Container.get(TestSupportRepository);
    const testLectureCreator: TestLectureCreator = Container.get(TestLectureCreator);
    const baseRepository: BaseRepository<Lecture> = new BaseRepository<Lecture>(nodePgTemplate, Lecture);

    beforeAll(async () => {
        await testSupportRepository.dropAll();
        await testSupportRepository.createTable();
    });

    afterEach(async () => {
        await testSupportRepository.deleteAll();
    });

    afterAll(async () => {
        await testSupportRepository.closeConnection();
    });

    it('findAll', async () => {
        //given
        const name = "test";
        await testLectureCreator.create(name);

        //when
        const result = await baseRepository.findAll();

        //then
        expect(result).toHaveLength(1);
        expect(result[0].name).toBe(name);
    });

    it('findOne', async () => {
        //given
        const name = "test";
        const lecture = await testLectureCreator.createAndReturn(name);

        //when
        const result = await baseRepository.findOne(lecture.id);

        //then
        expect(result.name).toBe(name);
    });

});
