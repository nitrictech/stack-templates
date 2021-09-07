package common

type Example struct {
	Name        string `json:"name"`
	Description string `json:"description"`
}

func (e Example) Map() map[string]interface{} {
	return structToMap(e)
}
