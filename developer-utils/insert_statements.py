import uuid
from datetime import datetime, timedelta
import psycopg2

def generate_insert_data():
    # Variables for generating statements
    start_due_date = datetime(2024, 6, 1)
    base_fee = 10.50
    base_total_amount = 150.75
    base_negotiation_number = 456789

    # Generate data
    insert_data = []
    for i in range(20000):
        id_str = str(uuid.uuid4())
        client_id = 1 + i
        due_date = (start_due_date + timedelta(days=15 * (i % 5))).strftime('%Y-%m-%d')
        negotiation_number = base_negotiation_number + i
        fee = base_fee - (i % 10) * 0.25
        total_amount = base_total_amount + (i % 50) * 5.00
        created_at = datetime.now().strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]

        insert_data.append((id_str, client_id, due_date, negotiation_number, fee, total_amount, created_at))

    return insert_data

def insert_data_to_db(insert_data):
    # Database connection parameters
    conn = psycopg2.connect(
        host="localhost",
        port="5432",
        database="pocs",
        user="pocs",
        password="poc123"
    )
    cur = conn.cursor()

    # Insert statement
    insert_query = """
    INSERT INTO public.charge
    (id, client_id, due_date, negotiation_number, fee, total_amount, active, created_at, updated_at, deleted_at)
    VALUES (%s, %s, %s, %s, %s, %s, true, %s, %s, NULL);
    """

    # Insert data
    for data in insert_data:
        cur.execute(insert_query, data + (data[6],))  # Duplicate created_at for updated_at

    # Commit and close
    conn.commit()
    cur.close()
    conn.close()

if __name__ == "__main__":
    insert_data = generate_insert_data()
    insert_data_to_db(insert_data)
