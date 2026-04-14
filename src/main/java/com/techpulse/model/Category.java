package com.techpulse.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

    @Entity
    @Table(name="Categories")
    public class Category
    {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private int id;

        @Column(name="name", nullable=false)
        private String name;

        //default constructor required by hibernate
        public Category()
        {
        }
            public Category(String name)
            {
                this.name=name;
            }

        public Category(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
