-- Creating a trigger function to generate a message when a payslip is approved
CREATE OR REPLACE FUNCTION public.create_payslip_approval_message()
    RETURNS TRIGGER AS $$
DECLARE
    employee_id BIGINT;
    employee_first_name VARCHAR;
    employee_code VARCHAR;
    institution_name VARCHAR := 'Rwanda Government ERP';
    message_content VARCHAR;
BEGIN
    -- Only proceed if status changes from PENDING to PAID
    IF (OLD.status = 'PENDING' AND NEW.status = 'PAID') THEN
        -- Fetch employee details
        SELECT e.id, COALESCE(e.first_name, 'Employee'), COALESCE(e.code, 'N/A')
        INTO employee_id, employee_first_name, employee_code
        FROM public.employees e
        WHERE e.id = NEW.employee_id;

        -- Check if employee exists
        IF employee_id IS NULL THEN
            RAISE NOTICE 'Employee with ID % not found for payslip approval', NEW.employee_id;
            RETURN NEW;
        END IF;

        -- Create message content with safe null handling
        message_content := 'Dear ' || employee_first_name ||
                           ', your salary for ' || COALESCE(NEW.month::TEXT, 'N/A') || '/' || COALESCE(NEW.year::TEXT, 'N/A') ||
                           ' from ' || institution_name ||
                           ' amounting to ' || COALESCE(NEW.net_salary::TEXT, '0') ||
                           ' has been credited to your account ' || employee_code || ' successfully.';

        -- Insert message into messages table
        INSERT INTO public.messages (
            employee_id,
            content,
            month,
            year,
            created_at,
            sent
        ) VALUES (
                     employee_id,
                     message_content,
                     NEW.month,
                     NEW.year,
                     NOW(),
                     FALSE
                 );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Dropping the trigger if it exists to prevent duplication
DROP TRIGGER IF EXISTS payslip_approval_trigger ON public.payslips;

-- Creating the trigger on the payslips table
CREATE TRIGGER payslip_approval_trigger
    AFTER UPDATE ON public.payslips
    FOR EACH ROW
EXECUTE FUNCTION public.create_payslip_approval_message();