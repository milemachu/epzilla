using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace epZillaEventProcessor.TriggerBase
{
  public  class Trigger
    {
        private string triggerId;
        private string triggerData;
        private string triggerSource;
        private DateTime recievedTime;
        

        public string TriggerData
        {
            get
            {
                return triggerData;
            }
            set
            {
                triggerData = value;
            }
        }
        

        public string TriggerSource
        {
            get
            {
                return triggerSource;
            }
            set
            {
                triggerSource = value;
            }
        }
        

        public DateTime RecievedTime1
        {
            get
            {
                return recievedTime;
            }
            set
            {
                recievedTime = value;
            }
        }

        public DateTime RecievedTime
        {
            get
            {
                return recievedTime;
            }
            set
            {
                recievedTime = value;
            }
        }


        public string TriggerId
        {
            get
            {
                return triggerId;
            }
            set
            {
                triggerId = value;
            }
        }

    }
}
