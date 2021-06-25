import React from "react";
import Admin from "react-crud-admin";
import Form from "react-jsonschema-form";

const url = "http://localhost:8080/api/v1/clients"

export default class Clients extends Admin {

    constructor() {
        super();
        this.name = "Contact";
        this.name_plural = "Contacts";
        // this.list_display_links = ["first_name", "last_name"];
        this.list_display = ["client_id", "first_name", "last_name", "accounts.length"];
    }

    get_queryset(page_number, list_per_page, queryset) {
        fetch(url, {method: "get", headers: {Accept: "application/json"}})
            .then(response => response.json())
            .then(json => this.set_queryset(json));
        return queryset;
    }

    form_submit(form) {
        let contact = form.formData;

        if (!form.edit) {
            console.log("send", contact);
            fetch(url, {method: "post", headers: {"Content-Type": "application/json"}, body: JSON.stringify(contact)})
                .then(response => {
                    if (response.status === 201) {
                        this.state.queryset.push(contact);
                        this.response_add();
                    }
                })
                .catch(console.error);
        }
    }

    get_form(object = null) {
        let schema = {
            title: this.name,
            type: "object",
            required: ["first_name", 'last_name', 'accounts'],
            properties: {
                first_name: {
                    type: "string",
                    title: "First Name",
                    default: ""
                },
                last_name: {
                    type: "string",
                    title: "Last Name",
                    default: ""
                },
                accounts: {
                    type: "array",
                    title: "Accounts",
                    items: {
                        type: "object",
                        required: ['account_num', 'account_type', 'balance'],
                        properties: {
                            account_num: {
                                type: "string",
                                title: "Account number",
                                default: (Math.floor(1000000 * Math.random()) + 1).toString()
                            },
                            account_type: {
                                type: "string",
                                title: "Account type",
                                enum: ['card/simple'],
                                default: 'card/simple'
                            },
                            balance: {
                                type: 'number',
                                title: 'Balance',
                                minimum: 0
                            }
                        }
                    }

                }
            }
        };

        if (!object) {
            return <Form schema={schema} onSubmit={this.form_submit.bind(this)}/>
        } else {
            return <Form schema={schema} formData={object} onSubmit={this.form_submit.bind(this)}/>
        }
    }

    render_list_view() {
        return (
            <div>
                {this.render_add_button()}
                {this.render_below_add_button()}
                {this.render_table()}
                {this.render_below_table()}
                {this.render_progress(this.state.loading)}
                {this.render_below_progress()}
                {this.render_pagination()}
            </div>
        );
    }


}