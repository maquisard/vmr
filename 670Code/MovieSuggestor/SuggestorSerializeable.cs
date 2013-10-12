using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;

namespace MovieSuggestor
{
    public interface SuggestorSerializeable
    {
        List<KeyValuePair<string, string>> Attributes { get; set; }
    }

    [Serializable]
    [XmlType(TypeName = "Attribute")]
    public struct KeyValuePair<K, V>
    {
        public KeyValuePair(K k, V v) : this() { Key = k; Value = v; }

        [XmlAttribute]
        public K Key
        { get; set; }

        [XmlAttribute]
        public V Value
        { get; set; }
    }
}
