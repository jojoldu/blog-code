import {Ticket} from "./Ticket";
import {Invitation} from "./Invitation";

export class Bag {
    private amount: number;
    private invitation: Invitation;
    private ticket: Ticket;

    constructor() {
    }

    public hold(ticket: Ticket): number {
        if (this.hasInvitation()) {
            this.setTicket(ticket);
            return 0;
        } else {
            this.setTicket(ticket);
            this.minusAmount(ticket.fee);
            return ticket.fee;
        }
    }

    setTicket(value: Ticket): void {
        this.ticket = value;
    }

    public hasInvitation(): boolean {
        return this.invitation != null;
    }

    public minusAmount(amount: number): void {
        this.amount -= amount;
    }

}
