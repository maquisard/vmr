package vrec.data;

import flexjson.JSONSerializer;

public interface JSONFilterable 
{
	public JSONSerializer filter(JSONSerializer serializer);
}
