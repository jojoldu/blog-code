import {Ticket} from "./Ticket";

export class TicketOffice {
    private amount: number;
    private tickets: Ticket[];

    get ticket(): Ticket {
        if (this.tickets.length == 0) {
            return null;
        }

        return this.tickets.pop();
    }

    public minusAmount(amount: number): void {
        this.amount -= amount;
    }

    public plusAmount(amount: number): void {
        this.amount += amount;
    }
}
