import assert from 'assert';

export class Dice {
    number: number;

    constructor(number: number) {
        this.number = number;
    }

    getMovePoint(): number {
        assert(this.number >= 1 && this.number <= 6, '주사위는 1~6까지만 존재합니다.');
        return this.number * 2;
    }
}
