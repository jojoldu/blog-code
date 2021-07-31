import {PageRequest} from "../../../src/controller/PageRequest";

describe('PageRequest', () => {
    it.each([
        [1, 10, 0],
        [2, 10, 10],
        [3, 10, 20],
        [1, 20, 0],
        [2, 20, 20],
    ])('pageNo=%i, pageSize=%i 이면 offset=%i', (pageNo, pageSize, offset) => {
        expect(new MockPageRequest(pageNo, pageSize).getOffset()).toBe(offset);
    })
})

class MockPageRequest extends PageRequest {

    constructor(pageNo: number, pageSize: number) {
        super();
        super.setPageNoAndSize(pageNo, pageSize);
    }
}
