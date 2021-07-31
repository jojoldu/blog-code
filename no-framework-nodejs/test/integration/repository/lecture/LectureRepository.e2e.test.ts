import "reflect-metadata"

import {LectureRepository} from "../../../../src/repository/lecture/LectureRepository";
import {Container} from "typedi";
import {NodePgTemplate} from "../../../../src/config/database/NodePgTemplate";
import {TestSupportRepository} from "../../TestSupportRepository";
import {NumberUtil} from "../../../../src/util/NumberUtil";
import {TestLectureCreator} from "../../TestLectureCreator";
import {LecturesRequest} from "../../../../src/controller/lecture/dto/LecturesRequest";
import {LectureCategory} from "../../../../src/entity/lecture/LectureCategory";

describe('LectureRepository', () => {
    const lectureRepository:LectureRepository = Container.get(LectureRepository);
    const nodePgTemplate: NodePgTemplate = Container.get(NodePgTemplate);
    const testSupportRepository: TestSupportRepository = Container.get(TestSupportRepository);
    const testLectureCreator: TestLectureCreator = Container.get(TestLectureCreator);

    beforeAll(async () => {
        await testSupportRepository.dropAll();
        await testSupportRepository.createTable();
    });

    afterEach(async () => {
        await testSupportRepository.deleteAll();
    });

    afterAll(async () => {
        await nodePgTemplate.close();
    });

    it('testSupportRepository test', async () => {
        const result = await nodePgTemplate.query(`SELECT COUNT(*) FROM lecture`);
        const count = NumberUtil.toNumber(result[0].count);
        expect(count).toBe(0);
    });

    it('getLectures를 통해 count와 Lecture Array가 반환된다', async () => {
        //given
        const name = "test";
        await testLectureCreator.create(name);

        //when
        const result = await lectureRepository.getLectures(LecturesRequest.byQueryParam({name: name}));

        //then
        expect(result.count).toBe(1);
        expect(result.items[0].name).toBe(name);
    });

    it('getLectures 에서 count는 전체 count, Lecture Array는 pageSize만큼 반환된다', async () => {
        //given
        await testLectureCreator.create("test");
        await testLectureCreator.create("test1");
        await testLectureCreator.create("test2");

        //when
        const result = await lectureRepository.getLectures(LecturesRequest.byQueryParam({
            category: LectureCategory.WEB, pageSize:2
        }));

        //then
        expect(result.count).toBe(3);
        expect(result.items).toHaveLength(2);
    });

    it('getLecture 조건에 맞는 LectureItem이 반환된다', async () => {
        //given
        const name = "test";
        const id = await testLectureCreator.create(name);

        //when
        const result = await lectureRepository.getLecture(id);

        //then
        expect(result.name).toBe(name);
        expect(result.students).toHaveLength(0);
    });

});
