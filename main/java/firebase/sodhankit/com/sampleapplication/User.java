package firebase.sodhankit.com.sampleapplication;

public class User
    {


        public String name, number;
        public User(String name, String number)
        {
            this.name=name;
            this.number=number;
        }
        public User()
        {
        }
        @Override
        public String toString() {
            return name+"----"+number;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNumber(String number) {
            this.number = number;
        }

    }