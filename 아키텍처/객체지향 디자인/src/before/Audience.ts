import {Bag} from "./Bag";

export class Audience {
    private readonly _bag: Bag;

    constructor(bag: Bag) {
        this._bag = bag;
    }

    get bag(): Bag {
        return this._bag;
    }
}
