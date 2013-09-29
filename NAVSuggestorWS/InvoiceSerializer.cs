using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Xml;
using System.Xml.Serialization;

namespace NAVSuggestorWS
{
    public static class InvoiceSerializer
    {
        public static XmlDocument ObjectToXml(object objectToSerialize, Type objectType)
        {
            XmlSerializer serializer = new XmlSerializer(objectType);
            MemoryStream stream = new MemoryStream();
            serializer.Serialize(stream, objectToSerialize);
            stream.Position = 0; // Reset stream position after we have written to it.
            var streamReader = new StreamReader(stream);
            string serializedString = streamReader.ReadToEnd();
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(serializedString);
            return doc;
        }
    }
}