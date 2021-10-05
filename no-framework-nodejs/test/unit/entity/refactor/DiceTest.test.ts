import { Dice } from '../../../../src/entity/refactor/Dice';

describe('Dice Test', () => {
    it('7일 경우', () => {
        const dice = new Dice(6);
        dice.number = 7;

        const movePoint = dice.getMovePoint();
        expect(movePoint).toBe(12);
    });

    it('exception으로 보는지', () => {
        const dice = new Dice(6);
        dice.number = 7;

        let movePoint;
        try {
            movePoint = dice.getMovePoint();
        } catch (e) {
            console.log('exception으로 잡히나요?');
            throw new Error('move point error');
        }

        expect(movePoint).toBe(12);
    });
});
