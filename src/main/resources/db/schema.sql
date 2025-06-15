-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS public;

-- Create sequence for employees
CREATE SEQUENCE IF NOT EXISTS public.employees_id_seq;

-- Create employees table
CREATE TABLE IF NOT EXISTS public.employees (
                                                id BIGINT PRIMARY KEY DEFAULT nextval('public.employees_id_seq'),
    code VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    date_of_birth DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create employee_roles table
CREATE TABLE IF NOT EXISTS public.employee_roles (
                                                     employee_id BIGINT NOT NULL,
                                                     roles VARCHAR(50) NOT NULL,
    PRIMARY KEY (employee_id, roles),
    FOREIGN KEY (employee_id) REFERENCES public.employees(id) ON DELETE CASCADE
    );

-- Create sequence for employments
CREATE SEQUENCE IF NOT EXISTS public.employments_id_seq;

-- Create employments table
CREATE TABLE IF NOT EXISTS public.employments (
                                                  id BIGINT PRIMARY KEY DEFAULT nextval('public.employments_id_seq'),
    code VARCHAR(50) UNIQUE NOT NULL,
    employee_id BIGINT NOT NULL,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    base_salary DECIMAL(15, 2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE', 'TERMINATED')),
    joining_date DATE NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES public.employees(id) ON DELETE RESTRICT
    );

-- Create deductions table
CREATE TABLE IF NOT EXISTS public.deductions (
                                                 id BIGINT PRIMARY KEY DEFAULT nextval('public.employments_id_seq'),
    code VARCHAR(50) UNIQUE NOT NULL,
    deduction_name VARCHAR(100) NOT NULL,
    percentage DECIMAL(5, 2) NOT NULL CHECK (percentage >= 0 AND percentage <= 100)
    );

-- Create sequence for payslips
CREATE SEQUENCE IF NOT EXISTS public.payslips_id_seq;

-- Create payslips table
CREATE TABLE IF NOT EXISTS public.payslips (
                                               id BIGINT PRIMARY KEY DEFAULT nextval('public.payslips_id_seq'),
    employee_id BIGINT NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    year INTEGER NOT NULL CHECK (year >= 1900),
    net_salary DECIMAL(15, 2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'PAID', 'CANCELLED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES public.employees(id) ON DELETE RESTRICT
    );

-- Create sequence for messages
CREATE SEQUENCE IF NOT EXISTS public.messages_id_seq;

-- Create messages table
CREATE TABLE IF NOT EXISTS public.messages (
                                               id BIGINT PRIMARY KEY DEFAULT nextval('public.messages_id_seq'),
    employee_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
    year INTEGER NOT NULL CHECK (year >= 1900),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (employee_id) REFERENCES public.employees(id) ON DELETE RESTRICT
    );