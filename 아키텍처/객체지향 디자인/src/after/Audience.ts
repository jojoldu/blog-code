import {Bag} from "./Bag";
import {Ticket} from "./Ticket";

export class Audience {
    private readonly bag: Bag;

    constructor(bag: Bag) {
        this.bag = bag;
    }

    public buy(ticket: Ticket): number {
        return this.bag.hold(ticket);
    }
}
