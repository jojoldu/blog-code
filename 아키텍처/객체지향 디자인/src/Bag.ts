import {Ticket} from "./Ticket";
import {Invitation} from "./Invitation";

export class Bag {
    private amount: number;
    private invitation: Invitation;
    private ticket: Ticket;

    constructor() {
    }


    public hasInvitation(): boolean {
        return this.invitation != null;
    }

    public hasTicket(): boolean {
        return this.ticket != null;
    }

    public setTicket(ticket: Ticket): void {
        this.ticket = ticket;
    }

    public minusAmount(amount: number): void {
        this.amount -= amount;
    }

    public plusAmount(amount: number): void {
        this.amount += amount;
    }
}
