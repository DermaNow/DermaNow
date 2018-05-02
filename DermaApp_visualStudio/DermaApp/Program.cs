using System;
using System.Data;
using System.IO;
using System.Drawing;
using System.Windows.Forms;
//Add MySql Library
using MySql.Data.MySqlClient;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;

namespace DermaApp
{
    class DBConnect
    {
        private MySqlConnection connection;
        private string server;
        private string database;
        private string uid;
        private string password;
        //Constructor
        public DBConnect(String dB_pointer)
        {
            Initialize(dB_pointer);
        }
        //Initialize values
        private void Initialize(String dB_pointer)
        {
            if (dB_pointer == "Source")
            {
                // Source Database Connection details
                server = "localhost";
                database = "MYSQL";
                uid = "root";
                password = "";
                string connectionString;
                connectionString = "SERVER=" + server + ";" + "DATABASE=" +
                database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";
                connection = new MySqlConnection(connectionString);
            }
            else if (dB_pointer == "Trained")
            {
                // Trained Database Connection details
                server = "localhost";
                database = "MYSQL";
                uid = "root";
                password = "";
                string connectionString;
                connectionString = "SERVER=" + server + ";" + "DATABASE=" +
                database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";
                connection = new MySqlConnection(connectionString);
            }
        }
        //open connection to database
        private bool OpenConnection()
        {
            try
            {
                connection.Open();
                return true;
            }
            catch (MySqlException ex)
            {
                switch (ex.Number)
                {
                    //0: Cannot connect to server.
                    case 0:
                        MessageBox.Show("Cannot connect to server. Contact administrator");
                        break;
                    //1045: Invalid user name and/or password.
                    case 1045:
                        MessageBox.Show("Invalid username/password, please try again");
                        break;
                }
                return false;
            }
        }
        //Close connection
        private bool CloseConnection()
        {
            try
            {
                connection.Close();
                return true;
            }
            catch (MySqlException ex)
            {
                MessageBox.Show(ex.Message);
                return false;
            }
        }
        public void RunApp()
        {
            this.DermaDectect();
        }



        // update methods for both doctor and user 



        //Update MYSQL source table with image processed class value.
        private void Update(int img_id, string disease_name, string description, string medication, string precautions, string symptoms, string severity)
        {
            string query = "UPDATE dermanow.user_disease_img SET disease_name='" + disease_name + "',description='" + description + "',medication='" + medication + "',precautions='" + precautions + "',symptoms='" + symptoms + "',severity='" + severity + "' WHERE img_id=" + img_id;
            Console.WriteLine(query);
            //create mysql command
            MySqlCommand cmd = new MySqlCommand();
            //Assign the query using CommandText
            cmd.CommandText = query;
            //Assign the connection using Connection
            cmd.Connection = connection;
            //Execute query
            cmd.ExecuteNonQuery();
        }
        //Compare the MATLAB function result with disease table to fetch the matched disease and its medication
        private string[] Compare(String diseaseName)
        {




            // Select the matched disease
            string query = "SELECT disease_name, description, medication, precautions, symptoms, severity FROM dermadetection.skin WHERE disease_name ='" + diseaseName + "';";


            string[] result_str = new string[] { null, null, null, null, null, null };
            if (this.OpenConnection() == true)
            {
                //Create Command
                MySqlCommand cmd = new MySqlCommand(query, connection);
                //Create a data reader and Execute the command
                MySqlDataReader dataReader = cmd.ExecuteReader();
                //Read the data and store them in the list
                while (dataReader.Read())
                {
                    result_str[0] = dataReader["disease_name"].ToString();
                    result_str[1] = dataReader["description"].ToString();
                    result_str[2] = dataReader["medication"].ToString();
                    result_str[3] = dataReader["precautions"].ToString();
                    result_str[4] = dataReader["symptoms"].ToString();
                    result_str[5] = dataReader["severity"].ToString();
                }
                //close connection
                this.CloseConnection();
            }
            return result_str;
        }
        // To fetch patient's image and detect the disease
        private void DermaDectect()
        {
            try
            {
                string query = "SELECT img_id,img_url FROM dermanow.user_disease_img where disease_name is null";
                //Open connection
                if (this.OpenConnection() == true)
                {
                    //Create Command
                    MySqlCommand cmd = new MySqlCommand(query, connection);
                    //Create a data reader and Execute the command
                    MySqlDataAdapter dataAdapter = new MySqlDataAdapter(cmd);
                    var ds = new DataSet();
                    dataAdapter.Fill(ds, "dermanow.user_disease_img");

                    int c = ds.Tables["dermanow.user_disease_img"].Rows.Count;
                    //Read the data and get the image
                    // MessageBox.Show("Going to display image");
                    while (c > 0)
                    {
                        //BLOB is read into Byte array, then used to construct MemoryStream,
                        //then passed to PictureBox.
                        //url

                        //URL
                        // Byte[] byteBLOBData = new Byte[0];

                        String urlB = null;
                        urlB = (String)(ds.Tables["dermanow.user_disease_img"].Rows[c - 1]["img_url"]) + ".jpg";
                        string localPath = new Uri(urlB).AbsoluteUri;

                        WebRequest re = WebRequest.Create(localPath);
                        WebResponse respo = re.GetResponse();
                        Stream sr = respo.GetResponseStream();

                       

                            byte[] buffer = new byte[16 * 1024];
                            MemoryStream mss = new MemoryStream();
                            {
                                int read;
                                while ((read = sr.Read(buffer, 0, buffer.Length)) > 0)
                                {
                                    mss.Write(buffer, 0, read);
                                }
                              //  mss.ToArray();
                            
                        }

                        //MemoryStream ms = new MemoryStream();
                       // Image img = Image.FromFile(localPath);
                       // img.Save(ms, System.Drawing.Imaging.ImageFormat.Jpeg);
                        Byte[] imgeByte = mss.ToArray();
                        //   HttpWebRequest req = (HttpWebRequest)WebRequest.Create(urlB);
                        //  HttpWebResponse response = (HttpWebResponse)req.GetResponse();
                        // Stream rec = response.GetResponseStream();

                        //    byte[] imageB = File.ReadAllBytes(urlB);

                        // byteBLOBData = (Byte[])(ds.Tables["dermanow.user_disease_img"].Rows[c - 1]["img_url"]);

                        int image_id = (int)(ds.Tables["dermanow.user_disease_img"].Rows[c - 1]["img_id"]);
                        MemoryStream stmBLOBData = new MemoryStream(imgeByte);
                        Bitmap bm = new Bitmap(stmBLOBData);

                        // MemoryStream stmBLOBData = new MemoryStream(byteBLOBData);
                        //  Bitmap bitm = new Bitmap(stmBLOBData);


                        // Save the bitmap image in the same location where MATLAB function is stored.
                        bm.Save("C:/Users/Rahaf/Documents/MATLAB/leaned/skin.jpg", System.Drawing.Imaging.ImageFormat.Jpeg);
                        // Create the MATLAB instance
                        MLApp.MLApp matlab = new MLApp.MLApp();
                        object matlab_result = null;
                        // function location
                        matlab.Execute(@"cd C:/Users/Rahaf/Documents/MATLAB/leaned");
                        matlab.Feval("image_process_complete", 1, out matlab_result, "C:/Users/Rahaf/Documents/MATLAB/leaned/skin.jpg");
                        object[] result = matlab_result as object[];
                        //double matlab_value = 600;
                        String disease_name = result[0].ToString();
                        Console.WriteLine(disease_name);
                        DBConnect dB_1 = new DBConnect("Trained");
                        string[] result_str = dB_1.Compare(disease_name);


                        if (result_str[0] != null)
                        {

                            //another if statement to check is it a patient or doctor

                            //if patient 
                            this.Update(image_id, result_str[0], result_str[1], result_str[2], result_str[3], result_str[4], result_str[5]);


                            //if doctor
                            //call method UpdateDoctor

                            //else user
                            //call method UpdateUser


                        }
                        else
                        {
                            Console.WriteLine("No matching Disease found.");
                        }
                        // Console.ReadKey();
                        c = c - 1;
                    }
                    //close Connection
                    this.CloseConnection();
                }
            }
            catch (Exception ex)
            { MessageBox.Show(ex.Message); }
        }
    }
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("I am going to fetch image from first table.");
            Console.WriteLine("Press any key to proceed.");
            Console.ReadKey();
            DBConnect dB = new DBConnect("Source");
            dB.RunApp();
            //Console.WriteLine();
            Console.WriteLine("Thanks for your time");
            Console.WriteLine("Press any key to exit.");
            Console.ReadKey();
        }

       
    }

}

