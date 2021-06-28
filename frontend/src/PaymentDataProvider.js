import {fetchUtils} from 'react-admin';

export const resources = {
    clients: 'clients',
    accounts: 'accounts',
    payments: 'payments',
    paymentInfo: 'paymentinfo'
}

const baseUrl = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080/api/v1";
const httpClient = fetchUtils.fetchJson;

const providers = {
    [resources.clients]: {
        path: 'clients',
        getList(params) {
            return httpClient(`${baseUrl}/${this.path}`)
                .then(({json}) => ({
                    data: json.map(client => ({id: client.client_id, ...client})),
                    total: json.length,
                }));
        },
        getOne(id) {
            return httpClient(`${baseUrl}/${this.path}/${id}`)
                .then(({json}) => ({data: {id: json.client_id, ...json}}));
        },
        create(client) {
            return httpClient(`${baseUrl}/${this.path}`, {
                method: 'POST',
                body: JSON.stringify(client),
            }).then(({json}) => ({
                data: {...client, id: json.client_id, client_id: json.client_id},
            }));
        },
        getMany(ids) {
            return Promise.all(ids.map(id => this.getOne(`${baseUrl}/${this.path}/${id}`)))
                .then(values => ({data: values.map(value => value.data)}));
        }
    },
    [resources.accounts]: {
        path: 'clients/accounts',
        getAll() {
            return httpClient(`${baseUrl}/clients`)
                .then(({json}) => {
                    let accounts = json.map(client => client.accounts)
                        .reduce((prev, cur) => [...prev, ...cur])
                        .map(account => ({id: account.account_id, ...account}));
                    console.log(accounts);
                    return ({
                        data: accounts,
                        total: accounts.length,
                    });
                });
        },
        getForClientId(id) {
            return httpClient(`${baseUrl}/clients/accounts?client_id=${id}`, {'Accept': 'application/json'})
                .then(({json}) => {
                    return ({
                        data: json.map(account => ({id: account.account_id, ...account})),
                        total: json.length,
                    });
                });
        },
        getList(params) {
            console.log(params);
            if (params.filter.client_id) {
                return this.getForClientId(params.filter.client_id);
            } else {
                return this.getAll();
            }
        },
        getOne(id) {
            return this.getAll()
                .then(accounts => accounts.filter(account => account.id == id));
        },
        getMany(ids) {
            return Promise.all(ids.map(id => this.getOne(`${id}`)))
                .then(values => ({data: values.map(value => value.data)}));
        }
    },
    [resources.payments]: {
        path: 'payments',
        getList(params) {
            return httpClient(`${baseUrl}/${this.path}`, {'Accept': 'application/json'})
                .then(({json}) => {
                    return ({
                        data: json.map(payment => ({id: payment.payment_id, ...payment})),
                        total: json.length,
                    });
                });
        },
        create(payments) {
            console.log("Create ", payments);
            return httpClient(`${baseUrl}/${this.path}`, {
                method: 'POST',
                body: JSON.stringify(payments.payments)
            }).then(({json}) => {
                let data = json.map((p, i) => ({
                    ...(payments.payments[i]),
                    id: p.payment_id,
                    payment_id: p.payment_id,
                    status: p.status
                }));
                return (
                    {
                        data: data[0]
                    }
                );
            });
        }
    },
    [resources.paymentInfo]: {
        path: 'payments/info',
        getList(params) {
            const filter = params.filter;
            if (filter.payer_id && filter.recipient_id && filter.source_acc_id && filter.dest_acc_id) {
                return httpClient(`${baseUrl}/${this.path}`, {
                    method: 'POST',
                    body: JSON.stringify({
                        payer_id: filter.payer_id,
                        recipient_id: filter.recipient_id,
                        source_acc_id: filter.source_acc_id,
                        dest_acc_id: filter.dest_acc_id
                    }),
                }).then(({json}) => ({
                    data: json.map(p => ({...p, id: p.payment_id})),
                    total: json.length
                }));
            }
            return Promise.reject("Put data in filter");
        }
    }
}


export class DataProvider {

    getList(resource, params) {
        if (providers.hasOwnProperty(resource)) {
            return providers[resource].getList(params);
        }
        return Promise.reject(`Provider for resource ${resource} not found`);
    }

    getOne(resource, params) {
        if (providers.hasOwnProperty(resource)) {
            return providers[resource].getOne(params.id);
        }
        return Promise.reject(`Provider for resource ${resource} not found`);
    }

    getMany(resource, params) {
        if (providers.hasOwnProperty(resource)) {
            return providers[resource].getMany(params.ids);
        }
        return Promise.reject(`Provider for resource ${resource} not found`);
    }

    create(resource, params) {
        if (providers.hasOwnProperty(resource)) {
            return providers[resource].create(params.data);
        }
        return Promise.reject(`Provider for resource ${resource} not found`);
    }
}