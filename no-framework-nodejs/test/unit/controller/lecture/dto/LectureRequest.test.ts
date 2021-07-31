import {LecturesRequest} from "../../../../../src/controller/lecture/dto/LecturesRequest";
import {LectureCategory} from "../../../../../src/entity/lecture/LectureCategory";
import {LecturePublishStatus} from "../../../../../src/entity/lecture/LecturePublishStatus";

describe('LectureRequest', () => {
    describe('getWhereCondition', () => {
        it('아무것도 없는 경우 조건문은 status만 있다', () => {
            const request = new LecturesRequest();

            expect(request.getWhereCondition()).toBe(`WHERE publish_status = '${LecturePublishStatus.PUBLIC}'`);
        });

        it('1개 조건이 있는 경우 where 1개조건이 생성된다', () => {
            const name = "test";
            const request = LecturesRequest.byQueryParam({name: name});

            expect(request.getWhereCondition()).toBe(`where publish_status = '${LecturePublishStatus.PUBLIC}' AND name = '${name}'`);
        });

        it('2개 조건이 있는 경우 where and 조건이 생성된다', () => {
            const name = "test";
            const category = LectureCategory.APP;
            const request = LecturesRequest.byQueryParam({name: name, category: category});

            expect(request.getWhereCondition()).toBe(`where publish_status = '${LecturePublishStatus.PUBLIC}' AND name = '${name}' AND category = '${category}'`);
        });
    });

    describe('getOrderBy', () => {
        it('isAsc가 true인 경우엔 ORDER BY ASC로 출력된다', () => {
            const order = "name";
            const request = LecturesRequest.byQueryParam({order: order, isAsc: true});

            expect(request.getOrderByQuery()).toBe(`ORDER BY ${order} ASC`);
        });

        it('isAsc가 false인 경우엔 ORDER BY DESC로 출력된다', () => {
            const order = "name";
            const request = LecturesRequest.byQueryParam({order: order, isAsc: false});

            expect(request.getOrderByQuery()).toBe(`ORDER BY ${order} DESC`);
        });

    });

});
