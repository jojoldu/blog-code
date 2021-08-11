import {Ticket} from "./Ticket";
import {Audience} from "./Audience";

export class TicketOffice {
    private amount: number;
    private tickets: Ticket[];

    public sellTicketTo(audience: Audience): void {
        this.plusAmount(audience.buy(this.ticket));
    }

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
