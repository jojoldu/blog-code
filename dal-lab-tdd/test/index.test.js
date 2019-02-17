const OK = 200;

const handlers = {
    '/': () => {
        return {status: OK, body: this.message }
    },
    '/login': () => {
        this.message = 'Welcome';
        return { status: 302 };
    },
    '/logout': () => {
        this.message = '<button>Login</button>';
        return {status: 302};
    }
};

const app = {
    get(path) {
        return handlers[path]();
    }
};

describe('test', () => {
    test('welcome', () => {
        app.get('/login');
        const {status, body} = app.get('/');
        expect(status).toBe(OK);
        expect(body).toMatch(/Welcome/);
    });

});
